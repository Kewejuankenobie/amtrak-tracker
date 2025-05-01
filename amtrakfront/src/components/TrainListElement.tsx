"use client"

import {Train} from "../types.ts";
import {useState} from "react";
import {AdvancedMarker, Map, MapCameraChangedEvent, Pin} from "@vis.gl/react-google-maps";
import TrainModal from "./TrainModal.tsx";

function TrainListElement({train, onClick}: {train: Train, onClick: React.MouseEventHandler<HTMLDivElement>}) {

    const [extraDetails, setExtraDetails] = useState<boolean>(false);
    const position = {lat: train.latitude, lng: train.longitude};

    return (
        <>
            <div className="bg-cyan-100 w-md my-2 p-2 rounded-lg border-1 border-blue-200 shadow-md
            hover:bg-cyan-50 cursor-pointer active:bg-blue-50"
            onClick={onClick}>
                <p className="underline">Train {train.number}: {train.name}</p>
                <p>Destination: {train.destination}</p>
                <p>{train.speed} mph</p>
                {
                    train.last_station_delay == null ?
                        <p>No info on the last station delay</p> :
                        train.last_station_delay < 0 ?
                            <p>Left {train.last_station} {Math.round(-1 * train.last_station_delay / 60)} minutes late</p> :
                            <p>Left {train.last_station} on time</p>
                }
                {
                    train.next_station_delay == null ?
                        <p>No info on the next station delay</p> :
                        train.next_station_delay < 0 ?
                            <p>Estimated arrival at {train.next_station} {Math.round(-1 * train.next_station_delay / 60)} minutes late</p> :
                            <p>Estimated arrival at {train.next_station} on time</p>
                }
                {
                    extraDetails && train.latitude != null && train.longitude != null ?
                        <>
                            <p>Details</p>
                            <div className="h-50">
                                {/*<Map*/}
                                {/*    defaultZoom={10}*/}
                                {/*    center={position}*/}
                                {/*    mapId={train.id}*/}
                                {/*    onCameraChanged={(e: MapCameraChangedEvent) =>*/}
                                {/*        console.log("Maps camera changed", e.detail.center, 'zoom:', e.detail.zoom)}>*/}
                                {/*    <AdvancedMarker position={position}>*/}
                                {/*        <Pin background={'blue'} glyphColor={'cyan'} borderColor={'black'}/>*/}
                                {/*    </AdvancedMarker>*/}
                                {/*</Map>*/}
                            </div>
                            <button onClick={() => setExtraDetails(false)}
                            className="border-1">Minimize</button>
                        </>:
                            <div/>
                }
            </div>
        </>

    );
}

export default TrainListElement;