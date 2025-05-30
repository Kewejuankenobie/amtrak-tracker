package com.kiron.amtrakTracker.model.gtfs;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

@Data
@Entity
@IdClass(StopTimeId.class)
public class StopTimes {

    @Id
    private String trip_id;

    @Id
    private Integer stop_sequence;

    @Id
    private String arrival_time;

    private String departure_time;
    private String stop_id;


}
