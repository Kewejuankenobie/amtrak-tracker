import React, {useEffect, useState} from "react";
import {getTrainUpdates, searchTrain} from "../api/trainapi.ts";
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
    const [query, setQuery] = useState<string>("");

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
    }, [query === '']);

    const handleSearch = async (e) => {
        e.preventDefault();
        try {
            const obtainedTrains: Train[] = await searchTrain(query);
            setTrains(obtainedTrains);
        } catch (error) {
            console.log(error);
        }
    }

    return (
      <>
        <div className="flex flex-col items-center bg-white">
            <h1 className="font-bold text-3xl pb-2">Train Tracker</h1>
            <form onSubmit={handleSearch}>
                <div className="flex flex-row items-center bg-white">
                <input type="text"
                    placeholder="Search Train Name/Num"
                       className="shadow appearance-none rounded w-full px-2 py-1 border-1 border-gray-50"
                       value={query}
                       onChange={(e) => setQuery(e.target.value)}
                    />
                <button type="submit" className="bg-blue-200 p-2 m-2 border-1 border-blue-300 rounded-md shadow-md
                cursor-pointer hover:bg-blue-300">Search</button>
                </div>
            </form>
            {
                trains.map((train: Train) => <TrainListElement train={train} />)
            }
        </div>
      </>
    );
}

export default Home;