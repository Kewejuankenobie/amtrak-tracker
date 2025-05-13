import {ChangeEvent, FormEvent, useEffect, useRef, useState} from "react";
import {searchTrain} from "../api/trainapi.ts";
import TrainListElement from "../components/TrainListElement.tsx";
import {Train} from "../types.ts";
import TrainDetail from "../components/TrainDetail.tsx";

function Home() {

    const [trains, setTrains] = useState<Train[]>([]);
    const [curTrain, setCurTrain] = useState<Train | null>(null);
    const [query, setQuery] = useState<string>("");

    const dialogRef = useRef<HTMLDialogElement | null>(null);

    async function fetchTrains() {
        try {
            const obtainedTrains: Train[] = await searchTrain(query);
            console.log(obtainedTrains);
            setTrains(obtainedTrains);
        } catch (error) {
            console.log(error);
        }
    }

    useEffect(() => {
        fetchTrains();
    }, []);

    useEffect(() => {
        const timerID: number = setTimeout(() => {
           fetchTrains();
        }, 30000);

        return () => {
            clearTimeout(timerID);
        }
    }, [trains]);

    const handleSearch =
        async (e: FormEvent<HTMLFormElement> | ChangeEvent<HTMLInputElement>, searchQuery: string) => {
        e.preventDefault();
        try {
            const obtainedTrains: Train[] = await searchTrain(searchQuery);
            console.log(obtainedTrains);
            setTrains(obtainedTrains);
        } catch (error) {
            console.log(error);
        }
    }

    useEffect(() => {
        if (!curTrain) return;
        dialogRef.current?.showModal();
    }, [curTrain]);

    return (
        <>
            <div className="flex flex-col lg:h-[95vh] h-[90vh] items-center bg-main-bg overflow-y-scroll">
                <div className="flex flex-col items-center justify-center w-full">
                    <form onSubmit={(e) => handleSearch(e, query)} className="w-3/5">
                        <div className="flex w-full">
                            <input type="text"
                                   placeholder="Search Train Name or Number"
                                   className="shadow rounded-lg px-2 py-4 bg-white w-full my-10 outline-0"
                                   value={query}
                                   onChange={(e) => {
                                       setQuery(e.target.value);
                                       handleSearch(e, e.target.value);
                                   }}
                            />
                        </div>
                    </form>
                </div>
                <div className="w-full flex flex-col items-center">
                {
                    trains.map((train: Train) => <TrainListElement train={train}
                                                                   onClick={() => setCurTrain(train)}/>)
                }
                </div>
            </div>
            <dialog ref={dialogRef} className="m-auto backdrop:bg-black/50 backdrop:backdrop-blur-sm overflow-visible rounded-lg w-3/5 h-3/5
            open:animate-dialog drop-shadow-xl">
                <div className={`flex flex-col relative z-0 
                ${curTrain && curTrain.railroad == 'AMTRAK' ? 'bg-[#E0F2E1]' : 'bg-[#E0F2E1]'} rounded-lg justify-normal
                     w-full h-full p-10`}>
                    <TrainDetail train={curTrain}/>
                    <button onClick={() => {
                        dialogRef.current?.close()
                    }}
                            className="absolute -top-2 -right-2 w-7 h-7 z-1 flex justify-center items-center
                                bg-gray-200 rounded-full text-2xl cursor-pointer shadow-md">X
                        <span className="sr-only">Close</span>
                    </button>
                </div>
            </dialog>
        </>
    );
}

export default Home;