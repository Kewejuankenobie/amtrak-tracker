import {TimeboardRow} from "../types.ts";

function TimeboardRowComp({row}: {row: TimeboardRow}) {
    return (
        <>
            <div className={`flex flex-col bg-gray-50 border-2 rounded-lg justify-normal my-2`}>
                <div className={`flex flex-row border-b-1 justify-between px-2 items-center`}>
                    <p className="font-semibold text-xl">{row.name}</p>
                    <p>{row.date}</p>
                </div>
                <div className={`flex flex-row justify-between px-2 items-center`}>
                    <div className={`flex md:flex-row flex-col items-center`}>
                        <p className="lg:text-4xl text-xl pr-4 font-mono font-bold text-blue-500">{row.number}</p>
                        <p className="lg:text-4xl text-xl text-blue-500 wrap-anywhere">{row.destination}</p>
                    </div>
                    <div className={`grid grid-cols-3 grid-rows-2 m-2 md:visible collapse`}>
                        <p>Schd.</p>
                        <p className="p-1 border-1 bg-gray-200">{row.scheduled_arrival}</p>
                        <p className="p-1 border-1 bg-gray-200">{row.scheduled_departure}</p>
                        <p>Est.</p>
                        <p className={`p-1 border-1 ${row.late_arrival ? "bg-red-300" : "bg-blue-200"}`}>{row.arrival}</p>
                        <p className={`p-1 border-1 ${row.late_departure ? "bg-red-300" : "bg-blue-200"}`}>{row.departure}</p>
                    </div>
                    <div className={`grid grid-cols-2 grid-rows-2 m-2 md:hidden visible`}>
                        <p className="p-1 border-1 bg-gray-200">{row.scheduled_arrival}</p>
                        <p className="p-1 border-1 bg-gray-200">{row.scheduled_departure}</p>
                        <p className={`p-1 border-1 ${row.late_arrival ? "bg-red-300" : "bg-blue-200"}`}>{row.arrival}</p>
                        <p className={`p-1 border-1 ${row.late_departure ? "bg-red-300" : "bg-blue-200"}`}>{row.departure}</p>
                    </div>

                </div>
            </div>
        </>
    );
}

export default TimeboardRowComp;