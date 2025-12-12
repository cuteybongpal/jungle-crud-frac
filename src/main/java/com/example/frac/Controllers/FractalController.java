package com.example.frac.Controllers;

import com.example.frac.Entity.FractalMap;
import com.example.frac.Service.FractalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FractalController
{
    private final FractalService fractalService;

    @GetMapping("/fractal/getPage")
    @ResponseBody
    public List<FractalMap> getFractalBitMap(
            @RequestParam double centerX,
            @RequestParam double centerY,
            @RequestParam double scale
    )
    {
        System.out.println(String.format("request!!!!!(%f, %f)", centerX, centerY));
        ArrayList<FractalMap> fractalMapList = new ArrayList<FractalMap>();
        for (int i = -3; i <= 3; i++)
        {
            for (int j = 2; j >= -2; j--)
            {
                double x = centerX + (double)i / scale;
                double y = centerY + (double)j / scale;
                fractalMapList.add(fractalService.GetFractal(x, y, scale));
            }
        }
        return fractalMapList;
    }
    public FractalController(FractalService _fractalService)
    {
        fractalService = _fractalService;
    }
}
