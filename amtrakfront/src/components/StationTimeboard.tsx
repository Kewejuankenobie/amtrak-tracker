import {Timeboard} from "../types.ts";
import TimeboardRowComp from "./TimeboardRowComp.tsx";

function StationTimeboard({timeboard}: {timeboard: Timeboard | null}) {

    if (timeboard == null) {
        return null;
    }

    return (
        <>
            <div className={`h-full`}>
                <p>{timeboard.code}: {timeboard.name}</p>
                <p>Learn more about the station at the <a href={timeboard.website} className={`underline text-purple-500`}>{timeboard.code} website</a></p>
                <h2 className={`py-2`}>Train Schedule</h2>
                <div className={`grid grid-cols-8 gap-2 border-b-1 border-gray-300`}>
                    <p>Date</p>
                    <p>Number</p>
                    <p>Name</p>
                    <p>Destination</p>
                    <p>Scheduled Arrival</p>
                    <p>Scheduled Departure</p>
                    <p>Arrival</p>
                    <p>Departure</p>
                </div>
                <div className={`h-4/5 overflow-y-auto my-2`}>
                    {
                        timeboard.timeboard.map(row => <TimeboardRowComp row={row}/>)
                    }
                </div>
            </div>
        </>
    );
}

export default StationTimeboard;