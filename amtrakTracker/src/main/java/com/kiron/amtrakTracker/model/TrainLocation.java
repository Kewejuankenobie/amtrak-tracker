package com.kiron.amtrakTracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TrainLocation {

    private Integer latitude;
    private Integer longitude;
    private Double speed;
}
