package com.kiron.amtrakTracker.model;

import lombok.Data;

@Data
public class TimeboardRow {

    private Integer number;
    private String name;
    private String scheduled_arrival;
    private String scheduled_departure;
    private String arrival;
    private String departure;
    private String destination;
    private Boolean late_arrival;
    private Boolean late_departure;
}
