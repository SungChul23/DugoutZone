package com.kim.SpringStudy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@ToString
public class User {
    @Id
    @Column(name = "username" ,nullable = false)
    private String username;

    @Column(name = "password" , nullable = false)
    private String password;
    @Column(name = "displayName" , nullable = false)
    private String displayName;

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    List<Sales> sales = new ArrayList<>();

}