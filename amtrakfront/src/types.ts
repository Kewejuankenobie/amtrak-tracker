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