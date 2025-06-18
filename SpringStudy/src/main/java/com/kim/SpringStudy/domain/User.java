package com.kim.SpringStudy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class User {
    @Id
    @Column(name = "username" ,nullable = false)
    private String username;

    @Column(name = "password" , nullable = false)
    private String password;
    @Column(name = "displayName" , nullable = false)
    private String displayName;

}