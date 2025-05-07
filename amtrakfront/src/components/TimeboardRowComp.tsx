import {TimeboardRow} from "../types.ts";

function TimeboardRowComp({row}: {row: TimeboardRow}) {
    return (
        <>
            <div className="grid grid-cols-8 space-x-2 justify-normal">
                <p>{row.date}</p>
                <p>{row.number}</p>
                <p>{row.name}</p>
                <p>{row.destination}</p>
                <p>{row.scheduled_arrival}</p>
                <p>{row.scheduled_departure}</p>
                {
                    row.arrival ? <p>{row.arrival}</p> : <p>No arrival info</p>
                }
                {
                    row.departure ? <p>{row.departure}</p> : <p>No departure info</p>
                }
            </div>
        </>
    );
}

export default TimeboardRowComp;