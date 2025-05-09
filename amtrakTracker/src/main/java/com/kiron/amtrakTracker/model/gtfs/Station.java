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

    //Should be using hash codes
    public boolean equals(Station other) {
        return this.id.equals(other.id);
    }

}
