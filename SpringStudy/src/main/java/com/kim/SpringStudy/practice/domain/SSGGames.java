package com.kim.SpringStudy.practice.domain;

import jakarta.persistence.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
public class SSGGames {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long id;

    @Column(name = "date", nullable = false)
    public String date;

    @Column(name = "day", nullable = false)
    public String day;

    @Column(name = "time", nullable = true)
    public String time;

    @Column(name = "stadium", nullable = true)
    public String stadium;

    @Column(name = "ssg_side", nullable = true)
    public String ssg_side;

    @Column(name = "opponent", nullable = true)
    public String opponent;

    @Column(name = "month", nullable = false)
    public String month;


}
