import {StationListEl, Timeboard} from "../types.ts";
import {ChangeEvent, FormEvent, useEffect, useRef, useState} from "react";
import {getAllStations, getTimeboard, searchStations} from "../api/stationapi.ts";
import StationListElement from "../components/StationListElement.tsx";
import StationTimeboard from "../components/StationTimeboard.tsx";

function StationInfo() {
    //This page shows information about all train stations

    const [query, setQuery] = useState<string>("");
    const [stations, setStations] = useState<StationListEl[]>([]);
    const [timeboard, setTimeboard] = useState<Timeboard | null>(null);
    const [loading, setLoading] = useState<boolean>(false);
    const [pageLoad, setPageLoad] = useState<boolean>(true);

    const dialogRef = useRef<HTMLDialogElement | null>(null);

    //We get all stations upon loading the page, if not, we keep trying until we do
    useEffect(() => {
        const fetchStations = async () => {
            setPageLoad(true);
            try {
                const obtainedStations: StationListEl[] = await getAllStations();
                console.log(obtainedStations);
                setStations(obtainedStations);
            } catch (error) {
                console.log(error);
                if (error instanceof TypeError) {
                    setTimeout(() => fetchStations(), 10000);
                }
            }
        }
        fetchStations();
    }, [])

    //When we have a list of stations, we are no longer loading
    useEffect(() => {
        if (pageLoad && stations.length > 0) {
            setPageLoad(false);
        }
    }, [stations]);

    const handleSearch =
        async (e: FormEvent<HTMLFormElement> | ChangeEvent<HTMLInputElement>, searchQuery: string) => {
        //Handles the search of stations, giving a station list of search results

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
        //Given a station we clicked, we get the timeboard for that station

        setLoading(true);
        try {
            const obtainedTimeboard: Timeboard = await getTimeboard(station.code);
            setTimeboard(obtainedTimeboard);
            console.log(obtainedTimeboard);

        } catch (error) {
            console.log(error);
        } finally {
            setLoading(false);
        }
    }

    //Upon a timeboard and loading change, we open the dialog box that shows the detail
    useEffect(() => {
        if (!timeboard && !loading) return;
        dialogRef.current?.showModal();
    }, [timeboard, loading]);

    return (
        <>
            <div className="flex flex-col h-[95vh] items-center bg-[#f5f9f6] overflow-y-scroll">

                {/*Search bar*/}
                <div className="flex flex-col items-center justify-center w-full">
                    <form onSubmit={(e) => handleSearch(e, query)} className="md:w-3/5 w-4/5">
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

                {/*Station list elements*/}
                {
                    pageLoad ? <p className={`animate-pulse font-bold text-2xl text-green-900`}>Loading ...</p> :
                <div className="w-full flex flex-col items-center">
                    {
                        stations.map((station: StationListEl) => <StationListElement station={station}
                                                                                     onClick={() => getTimeboardFromStation(station)}/>)
                    }
                </div>
                }
            </div>

            {/*Station detail dialog box*/}
            <dialog ref={dialogRef} className="m-auto backdrop:bg-black/50 backdrop:backdrop-blur-sm overflow-visible rounded-lg w-4/5 h-4/5
            open:animate-dialog drop-shadow-xl">
                <div className={`flex flex-col relative z-0 ${
                    timeboard && 'bg-[#E0F2E1]'
                } 
                rounded-lg justify-normal
                     w-full h-full md:p-6 p-2`}>
                    {
                     loading ?
                         <p className={`animate-pulse font-bold text-3xl text-green-900`}>Loading Station Info ...</p> :
                         <StationTimeboard timeboard={timeboard}/>
                    }
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

export default StationInfo;