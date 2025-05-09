package com.kiron.amtrakTracker.model.gtfs;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Route {

    @Id
    private String route_id;

    private String route_name;
}
