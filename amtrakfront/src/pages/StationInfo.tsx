import {StationListEl, Timeboard} from "../types.ts";
import {ChangeEvent, FormEvent, useEffect, useRef, useState} from "react";
import {getAllStations, getTimeboard, searchStations} from "../api/stationapi.ts";
import StationListElement from "../components/StationListElement.tsx";
import StationTimeboard from "../components/StationTimeboard.tsx";

function StationInfo() {
    const [query, setQuery] = useState<string>("");
    const [stations, setStations] = useState<StationListEl[]>([]);
    const [timeboard, setTimeboard] = useState<Timeboard | null>(null);

    const dialogRef = useRef<HTMLDialogElement | null>(null);

    useEffect(() => {
        const fetchStations = async () => {
            try {
                const obtainedStations: StationListEl[] = await getAllStations();
                console.log(obtainedStations);
                setStations(obtainedStations);
            } catch (error) {
                console.log(error);
            }
        }
        fetchStations();
    }, [])

    const handleSearch =
        async (e: FormEvent<HTMLFormElement> | ChangeEvent<HTMLInputElement>, searchQuery: string) => {
            e.preventDefault();
            try {
                const obtainedStations: StationListEl[] = await searchStations(searchQuery);
                console.log(obtainedStations);
                setStations(obtainedStations);
            } catch (error) {
                console.log(error);
            }
        }

    const getTimeboardFromStation = async (station: StationListEl)=> {
        try {
            const obtainedTimeboard: Timeboard = await getTimeboard(station.code);
            setTimeboard(obtainedTimeboard);
            console.log(obtainedTimeboard);
        } catch (error) {
            console.log(error);
        }
    }

    useEffect(() => {
        if (!timeboard) return;
        dialogRef.current?.showModal();
    }, [timeboard]);

    return (
        <>
            <div className="flex flex-col h-[94vh] items-center bg-gray-50 overflow-y-scroll">
                <div className="flex flex-col items-center justify-center w-full">
                    <form onSubmit={(e) => handleSearch(e, query)} className="w-3/5">
                        <div className="flex w-full">
                            <input type="text"
                                   placeholder="Search Station Code or Name"
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
                        stations.map((station: StationListEl) => <StationListElement station={station}
                                                                                     onClick={() => getTimeboardFromStation(station)}/>)
                    }
                </div>
            </div>
            <dialog ref={dialogRef} className="m-auto backdrop:bg-black/50 overflow-visible rounded-lg w-4/5 h-4/5">
                <div className={`flex flex-col relative z-0 bg-cyan-50 border-2 rounded-lg justify-normal
                     w-full h-full md:p-6 p-2`}>
                    <StationTimeboard timeboard={timeboard}/>
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

export default StationInfo;