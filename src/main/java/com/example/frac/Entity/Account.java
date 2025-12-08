package com.example.frac.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String username;
    private String password;

}
