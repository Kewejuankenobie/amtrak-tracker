import {ChangeEvent, FormEvent, useEffect, useRef, useState} from "react";
import {searchTrain} from "../api/trainapi.ts";
import TrainListElement from "../components/TrainListElement.tsx";
import {Train} from "../types.ts";

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
            <div className="flex flex-col items-center bg-white">
                <h1 className="font-bold text-3xl pb-2">Train Tracker</h1>
                <form onSubmit={(e) => handleSearch(e, query)}>
                    <div className="flex flex-row items-center bg-white">
                        <input type="text"
                               placeholder="Search Train Name or Number"
                               className="shadow appearance-none rounded w-full px-2 py-1 mb-4 mt-2 w-md border-1 border-gray-50"
                               value={query}
                               onChange={(e) => {
                                   setQuery(e.target.value);
                                   handleSearch(e, e.target.value);
                               }}
                        />
                        {/*<button type="submit" className="bg-blue-200 p-2 m-2 border-1 border-blue-300 rounded-md shadow-md*/}
                        {/*cursor-pointer hover:bg-blue-300">Search</button>*/}
                    </div>
                </form>
                {
                    trains.map((train: Train) => <TrainListElement train={train}
                                                                   onClick={() => setCurTrain(train)}/>)
                }
            </div>
            <dialog ref={dialogRef} className="m-auto">
                    <div className="flex flex-col bg-white border-1 rounded-lg justify-center items-center w-md p-10">
                        <p>{curTrain?.name}</p>
                        <button onClick={() => {
                            dialogRef.current?.close()
                        }}
                                className="border-1">X
                        </button>
                    </div>
            </dialog>
        </>
    );
}

export default Home;