package com.kiron.amtrakTracker.model.gtfs;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class TripAmtrak {

    @Id
    private String trip_id;

    private String route_id;
    private Integer number;
    private String destination;
}
