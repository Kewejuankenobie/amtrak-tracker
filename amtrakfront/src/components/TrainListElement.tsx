"use client"

import {Train} from "../types.ts";

function TrainListElement({train, onClick}: {train: Train, onClick: React.MouseEventHandler<HTMLDivElement>}) {

    return (
        <>
            <div className={`bg-[#E0F2E1] my-4 ${train.railroad == 'AMTRAK' ? ' active:bg-blue-50 hover:bg-[#E8EDF0]' : 
                'hover:bg-[#EEF0E8] active:bg-amber-50'} md:w-3/5 w-4/5 p-6 rounded-lg hover:-translate-y-1 hover:drop-shadow-lg duration-75
            shadow-md
            cursor-pointer`}
            onClick={onClick}>
                <p className={`font-semibold mb-2 text-lg ${train.railroad == 'AMTRAK' ? 'text-black' : 'text-black'}`}>{
                    train.railroad == 'AMTRAK' ? 'Amtrak' : 'VIA Rail'
                } Train {train.number}: {train.name}</p>
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