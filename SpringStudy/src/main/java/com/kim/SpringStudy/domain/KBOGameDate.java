package com.kim.SpringStudy.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "kbogame_date")
public class KBOGameDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String gameDate;
    private String time;
    private String matchup;
    private String stadium;

}
