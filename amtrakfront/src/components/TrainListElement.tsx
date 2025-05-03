"use client"

import {Train} from "../types.ts";

function TrainListElement({train, onClick}: {train: Train, onClick: React.MouseEventHandler<HTMLDivElement>}) {

    return (
        <>
            <div className="bg-cyan-100 w-3/5 my-2 p-2 rounded-lg border-1 border-blue-200 shadow-md
            hover:bg-cyan-50 cursor-pointer active:bg-blue-50"
            onClick={onClick}>
                <p className="font-semibold mb-2 text-lg text-blue-900">Train {train.number}: {train.name}</p>
                <div className="text-gray-700">
                    <p>Destination: {train.destination}</p>
                    {
                        train.next_station_delay == null ?
                            <p>No info on the next station</p> :
                            train.next_station_delay < 0 ?
                                <p>Next Stop, {train.next_station}, Estimated {Math.round(-1 * train.next_station_delay / 60)} minutes late</p> :
                                <p>Next Stop, {train.next_station}, Estimated on time arrival</p>
                    }
                </div>
            </div>
        </>

    );
}

export default TrainListElement;