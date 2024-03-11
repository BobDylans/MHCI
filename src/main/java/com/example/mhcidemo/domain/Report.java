package com.example.mhcidemo.domain;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class Report {

    private Long reportID;

    private Long userID; // This should be a foreign key reference to a User entity

    private String description;

    private Date reportDate = new Date();

    // Assuming that you store the location as a String. You may want to use a more complex type.
    private String location;

    // Getters and Setters
}

