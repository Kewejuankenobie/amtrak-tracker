package com.kiron.amtrakTracker.model;

import lombok.Data;

@Data
public class TimeboardRow {

    private Integer number;
    private String name;
    private String arrival;
    private String departure;
    private Boolean is_late;
}
