import React from "react";

export type Train = {
    id: string;
    number: number;
    name: string;
    railroad: string;
    destination: string;
    latitude?: number;
    longitude?: number;
    speed?: number;
    last_station?: string;
    last_station_delay?: number;
    next_station?: string;
    next_station_delay?: number;
    scheduled_arrival?: string;
    Component: React.ComponentType;
}

export type StationListEl = {
    code: string;
    name: string;
    website: string;
    admin_area: string;
    Component: React.ComponentType;
}

export type Timeboard = {
    code: string;
    name: string;
    website: string;
    admin_area: string;
    timeboard: TimeboardRow[];
    Component: React.ComponentType;
}

export type TimeboardRow = {
    date: string;
    number: number;
    name: string;
    scheduled_arrival: string;
    scheduled_departure: string;
    arrival?: string;
    departure?: string;
    destination: string;
    late_arrival: boolean;
    late_departure: boolean;
    Component: React.Component;
}