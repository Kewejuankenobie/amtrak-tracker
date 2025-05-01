import React from "react";

export type Train = {
    id: string;
    number: number;
    name: string;
    destination: string;
    latitude?: number;
    longitude?: number;
    speed?: number;
    last_station?: string;
    last_station_delay?: number;
    next_station?: string;
    next_station_delay?: number;
    Component: React.ComponentType;
}