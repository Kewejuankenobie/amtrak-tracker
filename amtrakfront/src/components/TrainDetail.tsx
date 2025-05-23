"use client"

import {Train} from "../types.ts";
import {AdvancedMarker, Map, MapCameraChangedEvent, Pin} from "@vis.gl/react-google-maps";

function DetailPart1({train}: {train: Train}) {
    return (
        <>
                <p className={`font-bold ${train.railroad == 'AMTRAK' ? 'text-blue-900' : 'text-amber-700'} 
                    mb-2 md:text-2xl text-lg`}>Train {train.number}: {train.name}</p>
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
                            <p>Est. arrival at {train.next_station}: {train.scheduled_arrival} {Math.round(-1 * train.next_station_delay / 60)} minutes late</p> :
                            <p>Est. arrival at {train.next_station}: {train.scheduled_arrival} on time</p>
                }
        </>
    )
}

function TrainDetail({train}: {train: Train | null}) {

    if (train == null) return null;

    if (train.latitude == null || train.longitude == null) return (
        <>
            <DetailPart1 train={train} />
        </>
    );

    const position = {lat: train.latitude, lng: train.longitude};

    return (
        <>
            <div className="h-full overflow-y-hidden mb-10 md:text-md text-sm">
                <DetailPart1 train={train}/>
                <div className="h-3/5">
                    <p>Latitude: {position.lat}</p>
                    <p>Longitude: {position.lng}</p>
                    <div className="h-full mt-2">
                        <Map
                            defaultZoom={12}
                            center={position}
                            mapId={train.id}
                            onCameraChanged={(e: MapCameraChangedEvent) =>
                                console.log("Maps camera changed", e.detail.center, 'zoom:', e.detail.zoom)}>
                            <AdvancedMarker position={position}>
                                <Pin background={'green'} glyphColor={'white'} borderColor={'black'}/>
                            </AdvancedMarker>
                        </Map>
                    </div>
                </div>
            </div>
            </>

        );
}

            export default TrainDetail;