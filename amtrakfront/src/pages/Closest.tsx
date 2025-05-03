import {useEffect, useState} from "react";
import {Train} from "../types.ts";
import {getClosestTrains} from "../api/trainapi.ts";
import TrainListElement from "../components/TrainListElement.tsx";

function Closest() {

    const [trains, setTrains] = useState<Train[]>([]);
    const [curTrain, setCurTrain] = useState<Train | null>(null);
    const [locationAllowed, setLocationAllowed] = useState<boolean>(false);

    async function displayTrains(position: GeolocationPosition) {
        const newTrains = await getClosestTrains(position);
        setTrains(newTrains);
        setLocationAllowed(true);
    }

    function setError(error: GeolocationPositionError) {
        switch (error.code) {
            case error.PERMISSION_DENIED:
                setLocationAllowed(false);
                break;
        }
    }

    useEffect(() => {
        const getClosestTrains = async () => {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(displayTrains, setError)
            } else {
                alert("Geolocation is not supported on this browser");
            }
        }
        getClosestTrains();
    }, []);

    return (
        <>
            <div className="bg-gray-50">
                {
                    !locationAllowed ? <p className="ml-4">Location Services Must Be Enabled To Use This Feature</p>
                        : <div className="w-full flex flex-col items-center">
                        <h1 className="text-3xl font-bold text-center">Nearby Trains</h1>
                            {
                                trains.map((train: Train) => <TrainListElement train={train}
                                                                               onClick={() => setCurTrain(train)}/>)
                            }
                        </div>
                }
            </div>

        </>
    );
}

export default Closest;