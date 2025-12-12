package com.example.frac.Service;

import com.example.frac.Entity.FractalMap;
import com.example.frac.Repository.IFractalMapRepository;
import org.springframework.stereotype.Service;

import java.util.BitSet;

@Service
public class FractalService {
    private final IFractalMapRepository repo;

    public FractalMap GetFractal(double x, double y, double scale) {
        //DB에 연산결과가 있을 때
        if (repo.existsByXAndYAndScale(x, y, scale))
            return repo.findByXAndYAndScale(x, y, scale);
        //없을 때,
        FractalMap fractalMap = new FractalMap();
        fractalMap.setX(x);
        fractalMap.setY(y);
        fractalMap.setScale(scale);
        BitSet bitset = new BitSet();
        int idx = 0;
        for (int i = 50; i > -50; i--) {
            for (int j = -50; j < 50; j++) {
                double r = x + ((double) i / (100 * scale));
                double c = y + ((double) j / (100 * scale));
                //발산인지 수렴인지
                boolean isbounded = true;
                double rr = 0;
                double cc = 0;
                //최대 100번 실행
                for (int iter = 0; iter < 100; iter++) {
                    double tempr = rr * rr - cc * cc + r;
                    double tempc = rr * cc * 2 + c;
                    rr = tempr;
                    cc = tempc;

                    // |z|^2 > 2 라면 발산
                    if (rr*rr + cc*cc > 4) {
                        isbounded = false;
                        break;
                    }
                }
                bitset.set(idx, !isbounded);
                idx++;
            }
        }
        fractalMap.setBitmap(bitset.toByteArray());
        repo.save(fractalMap);
        return fractalMap;
    }

    public FractalService(IFractalMapRepository _repo) {
        repo = _repo;
    }
}