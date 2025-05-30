package com.kiron.amtrakTracker.model.gtfs;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class StopTimeId implements Serializable {
    private String trip_id;
    private int stop_sequence;
    private String arrival_time;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StopTimeId stopTimeId = (StopTimeId) o;
        return trip_id.equals(stopTimeId.trip_id) && (stop_sequence == stopTimeId.stop_sequence) &&
                arrival_time.equals(stopTimeId.arrival_time);
    }

    @Override
    public int hashCode() {
        return 11 * (trip_id.hashCode() + stop_sequence + arrival_time.hashCode());
    }
}
