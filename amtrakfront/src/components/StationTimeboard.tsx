import {Timeboard} from "../types.ts";
import TimeboardRowComp from "./TimeboardRowComp.tsx";

function StationTimeboard({timeboard}: {timeboard: Timeboard | null}) {

    if (timeboard == null) {
        return null;
    }

    return (
        <>
            <div className={`h-full`}>
                <h1 className={`text-2xl text-blue-900 font-bold`}>{timeboard.code}: {timeboard.name}</h1>
                {timeboard.website && <p>Learn more about the station at the <a href={timeboard.website}
                                                           className={`underline text-purple-500`}>{timeboard.code} website</a></p>}
                <h2 className={`py-2 text-lg font-semibold`}>Train Schedule</h2>
                <div className={`h-2/3 lg:h-4/5 px-1 overflow-y-auto`}>
                    {
                        timeboard.timeboard.map(row => <TimeboardRowComp row={row}/>)
                    }
                </div>
            </div>
        </>
    );
}

export default StationTimeboard;