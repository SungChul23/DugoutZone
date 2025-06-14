package com.kim.SpringStudy.domain;

import jakarta.persistence.*;

@Entity
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id; //기본키

    public String title;
    public Integer price;
}