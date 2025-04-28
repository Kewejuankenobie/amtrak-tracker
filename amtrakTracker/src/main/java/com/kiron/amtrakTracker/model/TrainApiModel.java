package com.kiron.amtrakTracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TrainApiModel {
    /*
    Class for train object obtained from get request
     */
    private String train_id;
    private Integer number;
    private String name;
    //private Integer last_updated;
    private String destination;
    private TrainLocation location;
    private TrainStop[] stops;

}
