package com.kim.SpringStudy.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class KBOTeam {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;
    private String logoUrl;
    private String ticketUrl;
    private String homeCity;
    private String cssClass;


}
