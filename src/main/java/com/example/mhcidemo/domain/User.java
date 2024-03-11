package com.example.mhcidemo.domain;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class User {

    private Long userID;

    private String username;

    private String password;

    private String phone;

    private Date registrationDate = new Date();


    private String role;
    // Getters and Setters

}
