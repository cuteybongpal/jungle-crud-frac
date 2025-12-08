package com.example.frac.Entity;

import jakarta.persistence.*;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer Id;

    public String Title;
    public String Content;

    @ManyToOne
    public Account Author;
    public int Views;
}
