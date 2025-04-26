package com.kiron.amtrakTracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TrainArDep {
    private Integer variance;
    private String type;
}
