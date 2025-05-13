import {useEffect, useRef, useState} from "react";
import {Train} from "../types.ts";
import {getClosestTrains} from "../api/trainapi.ts";
import TrainListElement from "../components/TrainListElement.tsx";
import TrainDetail from "../components/TrainDetail.tsx";

function Closest() {

    const [trains, setTrains] = useState<Train[]>([]);
    const [curTrain, setCurTrain] = useState<Train | null>(null);
    const [locationAllowed, setLocationAllowed] = useState<boolean>(false);

    const dialogRef = useRef<HTMLDialogElement | null>(null);

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

    useEffect(() => {
        if (!curTrain) return;
        dialogRef.current?.showModal();
    }, [curTrain]);

    return (
        <>
            <div className="bg-[#f5f9f6] lg:h-[95vh] h-[90vh]">
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
            <dialog ref={dialogRef} className="m-auto backdrop:bg-black/50 overflow-visible rounded-lg w-3/5 h-3/5">
                <div className={`flex flex-col relative z-0 
                ${curTrain && curTrain.railroad == 'AMTRAK' ? 'bg-cyan-50' : 'bg-yellow-50'} border-2 rounded-lg justify-normal
                     w-full h-full p-10`}>
                    <TrainDetail train={curTrain}/>
                    <button onClick={() => {
                        dialogRef.current?.close()
                    }}
                            className="absolute -top-2 -right-2 w-7 h-7 z-1 flex justify-center items-center
                                bg-gray-200 rounded-full text-2xl cursor-pointer">X
                        <span className="sr-only">Close</span>
                    </button>
                </div>
            </dialog>

        </>
    );
}

export default Closest;