package com.kiron.amtrakTracker.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StationTimeboard {

    private String code;
    private String name;
    private String website;

    private List<TimeboardRow> timeboard;

    public StationTimeboard(String code, String name, String website) {
        this.code = code;
        this.name = name;
        this.website = website;
        this.timeboard = new ArrayList<>();
    }

    public void addRow(TimeboardRow row) {
        timeboard.add(row);
    }

    public Integer getLastTrainNumber() {
        if (timeboard.isEmpty()) {
            return -1;
        }
        return timeboard.getLast().getNumber();
    }
}
