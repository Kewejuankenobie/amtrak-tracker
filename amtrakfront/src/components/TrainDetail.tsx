"use client"

import {Train} from "../types.ts";
import {AdvancedMarker, Map, MapCameraChangedEvent, Pin} from "@vis.gl/react-google-maps";

function TrainDetail({train}: {train: Train | null}) {

    if (train == null) return null;

    const position = {lat: train.latitude, lng: train.longitude};

    return (
        <>
            <div className="h-full">
                <p className={`font-bold ${train.railroad == 'AMTRAK' ? 'text-blue-900' : 'text-amber-700'} 
                mb-2 text-2xl`}>Train {train.number}: {train.name}</p>
                <p>Destination: {train.destination}</p>
                <p>Speed: {train.speed} mph</p>
                {
                    train.last_station_delay == null ?
                        <p>No info on the last station</p> :
                        train.last_station_delay < 0 ?
                            <p>Left {train.last_station} {Math.round(-1 * train.last_station_delay / 60)} minutes late</p> :
                            <p>Left {train.last_station} on time</p>
                }
                {
                    train.next_station_delay == null || train.scheduled_arrival == null ?
                        <p>No info on the next station</p> :
                        train.next_station_delay < 0 ?
                            <p>Estimated arrival at {train.next_station} is {train.scheduled_arrival} {Math.round(-1 * train.next_station_delay / 60)} minutes late</p> :
                            <p>Estimated arrival at {train.next_station} is {train.scheduled_arrival} on time</p>
                }
                {
                    position.lat != null && position.lng != null &&
                    <div className="h-full">
                        <p>Latitude: {position.lat}</p>
                        <p>Longitude: {position.lng}</p>
                        <div className="h-3/5 mt-2">
                            <Map
                                defaultZoom={12}
                                center={position}
                                mapId={train.id}
                                onCameraChanged={(e: MapCameraChangedEvent) =>
                                    console.log("Maps camera changed", e.detail.center, 'zoom:', e.detail.zoom)}>
                                <AdvancedMarker position={position}>
                                    <Pin background={'blue'} glyphColor={'cyan'} borderColor={'black'}/>
                                </AdvancedMarker>
                            </Map>
                        </div>
                    </div>
                }
            </div>
        </>

    );
}

export default TrainDetail;