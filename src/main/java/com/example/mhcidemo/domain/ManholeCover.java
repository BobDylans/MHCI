package com.example.mhcidemo.domain;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class ManholeCover {


    private Long manholeCoverID;


    private String identifier;


    private String location;


    private Date installationDate = new Date();

    // Getters and Setters
}
