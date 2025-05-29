package com.kiron.amtrakTracker.model.gtfs;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Station {

    @Id
    private String id;

    private String code;
    private String name;
    private String website;
    private String time_zone;
    private String admin_area;

}
