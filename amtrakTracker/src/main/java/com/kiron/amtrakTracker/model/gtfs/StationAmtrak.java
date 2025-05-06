package com.kiron.amtrakTracker.model.gtfs;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class StationAmtrak {

    @Id
    private String id;

    private String name;
    private String website;

}
