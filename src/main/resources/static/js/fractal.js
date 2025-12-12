//현재 상태와 서버에서 프랙탈의 정보를 가지는 역할을 가짐
function base64ToUint8Array(base64) {
    const binary = atob(base64);              // Base64 → binary string
    const len = binary.length;
    const bytes = new Uint8Array(len);
    for (let i = 0; i < len; i++) {
        bytes[i] = binary.charCodeAt(i);      // 각 문자 → 0~255
    }
    return bytes;
}

class Fractal
{
    constructor()
    {
        this.scale = 1;
        this.centerX = 0;
        this.centerY = 0;
        this.prevX = 0;
        this.prevY = 0;
        this.drag = false;

        //버퍼 설정
        this.buffer = document.createElement('canvas');
        this.buffer.width = 700;
        this.buffer.height = 500;
        this.bufferCtx = this.buffer.getContext('2d');
        document.body.appendChild(this.buffer);
    }
}
// 얘는 canvas에 프랙탈을 보여주고, 줌 기능과 움직이는 기능을 가짐
class FractalViewer
{
    constructor(canvas)
    {
        this.canvas = canvas;
        this.ctx =  canvas.getContext('2d');
    }
    //마우스 기준으로 줌
    zoom(factor, fractal)
    {
        fractal.scale *= factor;
        this.draw(fractal);
    }

    move(dx, dy, fractal) 
    {
        fractal.centerX += dx;
        fractal.centerY += dy;
        this.draw(fractal);
    }

    draw(fractal) {
        this.ctx.setTransform(1, 0, 0, 1, 0, 0); // reset
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
        this.ctx.translate(this.canvas.width/2, this.canvas.height/2);
        this.ctx.scale(fractal.scale, -fractal.scale); // 2) 확대 + y축 뒤집기
        this.ctx.translate(-fractal.centerX, -fractal.centerY); // 3) 프랙탈 중심 위치로 이동

        this.ctx.drawImage(fractal.buffer, -3.5*100, -2.5*100);
    }
    setPixel(px, py, bit, pixels, width, height)
    {

        // 화면 밖이면 무시
        if (px < 0 || px >= width) return;
        if (py < 0 || py >= height) return;

        let idx = (py * width + px) * 4;  // RGBA 4바이트

        if (bit) {
            // 수렴: 흰색
            pixels[idx]     = 255; // R
            pixels[idx + 1] = 255; // G
            pixels[idx + 2] = 255; // B
        } else {
            // 발산: 검정
            pixels[idx]     = 0;
            pixels[idx + 1] = 0;
            pixels[idx + 2] = 0;
        }
        pixels[idx + 3] = 255; // A (불투명)
    }

}
//뷰어랑 프랙탈을 이용하는 놈임
class FractalController
{
    constructor(fractal, viewer)
    {
        this.fractal = fractal;
        this.viewer = viewer;
        this.bindingEvent();
    }

    bindingEvent()
    {
        //todo : 이벤트 연동
        this.viewer.canvas.addEventListener("mousedown", (e) =>{
            //todo : drag true로 만들기
            this.fractal.drag = true;
            this.fractal.prevX = e.movementX;
            this.fractal.prevY = e.movementY;
        });
        window.addEventListener("mousemove", (e)=>{
            //todo : move호출 
            if (!this.fractal.drag)
                return;
            let dx = e.movementX;
            let dy = e.movementY;
            this.viewer.move(-dx / this.fractal.scale, dy / this.fractal.scale, this.fractal);
        });
        window.addEventListener("mouseup", (e) =>{
            //todo : fractal을 다시 로드 isdrag를 false로 만들기
            this.fractal.drag = false;
            this.load_fractal();
        });
        this.viewer.canvas.addEventListener("wheel", (e) =>{
            //todo : zoom 땡기기, scale이 어느 정도 커지면 다시 로드해와야함!
            e.preventDefault();
            const factor = e.deltaY < 0 ? 1.1 : 0.9;
            this.fractal.scale *= factor;
            console.log(this.fractal.scale)
            this.viewer.zoom(factor, this.fractal);
        });

    }
    load_fractal()
    {
        //todo : 서버에서 POST 요청을 보내서 각 픽셀마다, 색을 가져옴.
        //0이면 흑색 1 이면 하얀색임
        // fetch("/fractal/get/cx")
        let roundedScale = 2 ** Math.round(Math.log2(this.fractal.scale));
        let roundedX = Math.round(this.fractal.centerX/100 * roundedScale) / roundedScale;
        let roundedY = Math.round(this.fractal.centerY/100 * roundedScale) / roundedScale;
        console.log(`center : (${roundedX}, ${roundedY})`)
        console.log(`scale : ${roundedScale}`)
        fetch(`/fractal/getPage?centerX=${roundedX}&centerY=${roundedY}&scale=${roundedScale}`)
        .then((res) => res.json())
        .then((data) =>{
            let imageData = this.fractal.bufferCtx.createImageData(this.fractal.buffer.width, this.fractal.buffer.height);
            let pixels = imageData.data;
            console.time("load");
            for (let i = 0; i < data.length; i++)
            {
                let x = data[i]['x'];
                let y = data[i]['y'];
                let scale = data[i]['scale']
                let array = base64ToUint8Array(data[i]['bitmap'])
                
                for (let j = 0; j < 100; j++)
                {
                    for (let k = 0; k < 100; k++)
                    {
                        let idx = 100 * j + k;
                        let array_idx = Math.floor(idx / 8);
                        let bit_mask = 1 << (idx % 8);
                        const bit = (array[array_idx] & bit_mask) ? 1 : 0;

                        const TILE = 100;
                        const ox = TILE / 2 - j;  // -50 ~ 49 (서버의 i 또는 j 역할)
                        const oy = k - TILE / 2;  // -50 ~ 49
                        const step = 1 / (TILE * scale); // 서버랑 똑같이

                        // 서버의 r, c에 대응되는 프랙탈 좌표
                        const fx = x + ox * step; // r 역할
                        const fy = y + oy * step; // c 역할

                        const screenScale = 100 * this.fractal.scale;

                        const px = (fx - roundedX) * screenScale
                                 + this.viewer.canvas.width / 2;

                        const py = (roundedY - fy) * screenScale
                                 + this.viewer.canvas.height / 2;

                        this.viewer.setPixel(Math.round(px), Math.round(py), bit, pixels, this.viewer.canvas.width, this.viewer.canvas.height);
                    }
                }
            }
            console.timeEnd("load");
            this.fractal.bufferCtx.clearRect(0,0, this.fractal.buffer.width, this.fractal.buffer.height);
            this.fractal.bufferCtx.putImageData(imageData, 0, 0);
            this.viewer.draw(this.fractal);
        });
    }
}
const canvas = $('#fractalCanvas')[0];
const ctx = canvas.getContext('2d')
ctx.translate(canvas.width/2, canvas.height/2);
ctx.scale(1, -1);

let frac = new Fractal();
let view = new FractalViewer(canvas)
let controller = new FractalController(frac, view);
controller.load_fractal();