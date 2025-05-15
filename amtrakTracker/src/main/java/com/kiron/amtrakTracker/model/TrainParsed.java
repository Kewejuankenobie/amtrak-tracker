package com.kiron.amtrakTracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class TrainParsed {

    @Id
    private String id;

    private Integer number;
    private String name;
    private String railroad;
    private String destination;
    private Double latitude;
    private Double longitude;
    private Double speed;
    private String last_station;
    private Integer last_station_delay;
    private String next_station;
    private Integer next_station_delay;
    private String scheduled_arrival;
    private Long arrival_epoch;

    private Boolean is_active;

    public TrainParsed(TrainApiModel train) {
        id = train.getTrain_id();
        number = train.getNumber();
        name = train.getName();
        destination = train.getDestination();
        is_active = true;
        if (train.getLocation() == null) {
            System.out.println("No Location");
            return;
        }
        latitude = train.getLocation().getLatitude();
        longitude = train.getLocation().getLongitude();
        speed = train.getLocation().getSpeed();
        railroad = train.getRailroad();

        TrainStop stationItr = null;
        for (TrainStop station: train.getStops()) {
            if (station.getArrive() == null) {
                stationItr = station;
                continue;
            }
            //First, we get the first station we have not arrived at yet
            if (station.getArrive().getType().equals("ESTIMATED")) {
                if (stationItr == null) {
                    stationItr = station;
                }
                //Set delay from the station we left and the estimated for the next
                //It is possible that the departure from the previous station is unknown
                if (stationItr.getDepart() == null) {
                    if (stationItr.getArrive() == null) {
                        last_station_delay = null;
                    } else {
                        last_station_delay = stationItr.getArrive().getVariance();
                    }
                } else {
                    last_station_delay = stationItr.getDepart().getVariance();
                }
                last_station = stationItr.getCode();
                next_station = station.getCode();
                next_station_delay = station.getArrive().getVariance();
                if (station.getSched_arrive() == null) {
                    if (station.getSched_depart() == null) {
                        return;
                    }
                    if (station.getDepart() == null) {
                        arrival_epoch = station.getSched_depart();
                    } else {
                        arrival_epoch = station.getSched_depart() - station.getDepart().getVariance();
                    }
                } else {
                    arrival_epoch = station.getSched_arrive() - next_station_delay;
                }
                return;
            }
            stationItr = station;
        }
    }
}
