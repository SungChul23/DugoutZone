package com.kim.SpringStudy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class SSG {
    @Id
    @Column(name = "number")
    public String number;

    @Column(name = "player")
    public String player;
    @Column(name = "position")
    public String position;

    @Column(name = "years")
    public String years;


}
