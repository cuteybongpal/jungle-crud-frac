package com.example.frac.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FractalMap
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private double x;
    private double y;
    private double scale;

    @Lob
    private byte[] bitmap;
}
