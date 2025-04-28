import React, {useEffect, useState} from "react";
import {getTrainUpdates} from "../api/trainapi.ts";
import TrainListElement from "../components/TrainListElement.tsx";

type Train = {
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

function Home() {

    const [trains, setTrains] = useState<Train[]>([]);

    useEffect(() => {
        const updateTrains = async () => {
            try {
                const obtainedTrains: Train[] = await getTrainUpdates();
                console.log(obtainedTrains);
                setTrains(obtainedTrains);
            } catch (error) {
                console.log(error);
            }
        }
        updateTrains();
    }, []);

    return (
      <>
        <div className="flex flex-col items-center bg-white">
            <h1 className="font-bold text-3xl pb-2">Train Tracker</h1>
            <p>Search</p>
            {
                trains.map((train: Train) => <TrainListElement train={train} />)
            }
        </div>
      </>
    );
}

export default Home;