package com.kiron.amtrakTracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TimeboardRow {

    private Integer number;
    private String name;
    private String scheduled_arrival;
    private String scheduled_departure;
    private String arrival;
    private String departure;
    private String destination;
    private String date;
    private Boolean late_arrival;
    private Boolean late_departure;
    private Long actual_time;

    public TimeboardRow(TimeboardRow row, boolean copyDate) {
        this.number = row.getNumber();
        this.name = row.getName();
        this.scheduled_arrival = row.getScheduled_arrival();
        this.scheduled_departure = row.getScheduled_departure();
        this.arrival = row.getArrival();
        this.departure = row.getDeparture();
        this.destination = row.getDestination();
        if (copyDate) {
            this.date = row.getDate();
        }
        this.late_arrival = row.getLate_arrival();
    }
}
