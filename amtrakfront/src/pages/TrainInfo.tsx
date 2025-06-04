import {ChangeEvent, FormEvent, useEffect, useRef, useState} from "react";
import {searchTrain} from "../api/trainapi.ts";
import TrainListElement from "../components/TrainListElement.tsx";
import {Train} from "../types.ts";
import TrainDetail from "../components/TrainDetail.tsx";

function TrainInfo() {
    //This page shows information about all trains

    const [trains, setTrains] = useState<Train[]>([]);
    const [curTrain, setCurTrain] = useState<Train | null>(null);
    const [query, setQuery] = useState<string>("");
    const [pageLoad, setPageLoad] = useState<boolean>(true);

    const dialogRef = useRef<HTMLDialogElement | null>(null);

    async function fetchTrains() {
        //Fetches trains by a search query, if no query is provided, it returns all trains
        try {
            const obtainedTrains: Train[] = await searchTrain(query);
            console.log(obtainedTrains);
            setTrains(obtainedTrains);
        } catch (error) {
            console.log(error);
        }
    }

    //At page loading, we fetch all trains
    useEffect(() => {
        fetchTrains();
    }, []);

    //When we have trains (or the list is updated), we stop loading the page, and update the trains again in 2 minutes
    useEffect(() => {
        if (pageLoad && trains.length > 0) {
            setPageLoad(false);
        }
        const timerID: number = setTimeout(() => {
           fetchTrains();
        }, 120000);

        return () => {
            clearTimeout(timerID);
        }
    }, [trains]);

    const handleSearch =
        async (e: FormEvent<HTMLFormElement> | ChangeEvent<HTMLInputElement>, searchQuery: string) => {
        //Handle the searching of trains, giving a train list of results

        e.preventDefault();
        try {
            const obtainedTrains: Train[] = await searchTrain(searchQuery);
            console.log(obtainedTrains);
            setTrains(obtainedTrains);
        } catch (error) {
            console.log(error);
        }
    }

    //Upon a train clicked, we open a dialog box to show the detail
    useEffect(() => {
        if (!curTrain) return;
        dialogRef.current?.showModal();
    }, [curTrain]);

    return (
        <>
            <div className="flex flex-col lg:h-[95vh] h-[85vh] items-center bg-main-bg overflow-y-scroll">

                {/*Search bar*/}
                <div className="flex flex-col items-center justify-center w-full">
                    <form onSubmit={(e) => handleSearch(e, query)} className="md:w-3/5 w-4/5">
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

                    {/*Train list elements*/}
                </div>
                {
                    pageLoad ? <p className={`animate-pulse font-bold text-2xl text-green-900`}>Loading ...</p> :
                    <div className="w-full flex flex-col items-center">
                    {
                        trains.map((train: Train) => <TrainListElement train={train}
                                                                       onClick={() => setCurTrain(train)}/>)
                    }
                    </div>
                }
            </div>

            {/*Train detail dialog box*/}
            <dialog ref={dialogRef} className="m-auto backdrop:bg-black/50 backdrop:backdrop-blur-sm overflow-visible rounded-lg w-4/5 h-4/5
            open:animate-dialog drop-shadow-xl">
                <div className={`flex flex-col relative z-0 
                ${curTrain && curTrain.railroad == 'AMTRAK' ? 'bg-[#E0F2E1]' : 'bg-[#E0F2E1]'} rounded-lg justify-normal
                     w-full h-full md:pt-6 md:px-6 pt-2 px-2`}>
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

export default TrainInfo;