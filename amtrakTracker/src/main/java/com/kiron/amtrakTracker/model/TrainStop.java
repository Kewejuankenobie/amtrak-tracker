package com.kiron.amtrakTracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TrainStop {

    private String code;
    private Long sched_arrive;
    private Long sched_depart;
    private TrainArDep arrive;
    private TrainArDep depart;
}
