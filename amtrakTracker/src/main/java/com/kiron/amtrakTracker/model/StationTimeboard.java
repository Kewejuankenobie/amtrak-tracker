package com.kiron.amtrakTracker.model;

import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class StationTimeboard {

    private String code;
    private String name;
    private String website;
    private String admin_area;

    private List<TimeboardRow> timeboard;

    public StationTimeboard(String code, String name, String website, String admin_area) {
        this.code = code;
        this.name = name;
        this.website = website;
        this.admin_area = admin_area;
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

    public void sortTimeboard() {
    //Sorts the timeboard rows by date and time
        timeboard.sort((o1, o2) -> {
            String o1DT;
            String o2DT;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
            if (o1.getDeparture() == null) {
                o1DT = o1.getDate() + formatter2.format(formatter.parse(o1.getScheduled_departure()));
            } else {
                o1DT = o1.getDate() + formatter2.format(formatter.parse(o1.getDeparture()));
            }
            if (o2.getDeparture() == null) {
                o2DT = o2.getDate() + formatter2.format(formatter.parse(o2.getScheduled_departure()));
            } else {
                o2DT = o2.getDate() + formatter2.format(formatter.parse(o2.getDeparture()));
            }
            return o1DT.compareTo(o2DT);
        });
    }
}
