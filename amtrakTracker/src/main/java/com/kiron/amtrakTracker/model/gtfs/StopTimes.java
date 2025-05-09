package com.kiron.amtrakTracker.model.gtfs;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class StopTimes {

    @Id
    private Long id;

    private String trip_id;
    private String departure_time;
    private String arrival_time;
    private String stop_id;
    private Integer stop_sequence;


}
