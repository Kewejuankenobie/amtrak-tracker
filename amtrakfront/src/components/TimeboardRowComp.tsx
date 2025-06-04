import {TimeboardRow} from "../types.ts";

function TimeboardRowComp({row, type}: {row: TimeboardRow, type: string}) {
    //Component that shows arrival and departure information on a train given a timeboard row

    return (
        <>
            <div className={`flex flex-col border-b-1 justify-normal my-2`}>
                <div className={`flex flex-row border-b-1 border-dashed border-gray-500 justify-between px-2 items-center`}>
                    <p className="font-semibold text-xl">{row.name}</p>
                    <p className={`ml-8`}>{row.date}</p>
                </div>
                <div className={`flex flex-row justify-between px-2 items-center`}>
                    <div className={`flex md:flex-row flex-col items-center
                    ${type.length == 3 ? 'text-blue-500' : 'text-amber-600'}`}>
                        <p className="lg:text-4xl text-xl lg:pr-4 pr-2 font-mono font-bold">{row.number}</p>
                        <p className="lg:text-4xl text-xl flex-wrap">{row.destination}</p>
                    </div>

                    {/*When the browser window is larger, then more text can be shown on the arrival and departures*/}
                    <div className={`grid grid-cols-3 grid-rows-3 m-2 md:visible collapse`}>
                        <div/>
                        <p className={`text-center`}>Arrive</p>
                        <p className={`text-center`}>Depart</p>
                        <p className={`text-right pr-4`}>Schd.</p>
                        <p className="p-1 border-1 rounded-tl-sm bg-gray-200">{row.scheduled_arrival}</p>
                        <p className="p-1 border-1 rounded-tr-sm bg-gray-200">{row.scheduled_departure}</p>
                        <p className={`text-right pr-4`}>Est.</p>
                        <p className={`p-1 border-1 rounded-bl-sm ${row.late_arrival ? "bg-red-300" : "bg-green-200"}`}>{row.arrival}</p>
                        <p className={`p-1 border-1 rounded-br-sm ${row.late_departure ? "bg-red-300" : "bg-green-200"}`}>{row.departure}</p>
                    </div>

                    {/*Arrival and departures for smaller screens*/}
                    <div className={`grid grid-cols-2 grid-rows-3 m-2 md:hidden visible min-w-2/5 text-sm`}>
                        <p className={`text-center`}>Arrive</p>
                        <p className={'text-center'}>Depart</p>
                        <p className="p-1 border-1 rounded-tl-sm bg-gray-200">{row.scheduled_arrival}</p>
                        <p className="p-1 border-1 rounded-tr-sm bg-gray-200">{row.scheduled_departure}</p>
                        <p className={`p-1 border-1 rounded-bl-sm ${row.late_arrival ? "bg-red-300" : "bg-green-200"}`}>{row.arrival}</p>
                        <p className={`p-1 border-1 rounded-br-sm ${row.late_departure ? "bg-red-300" : "bg-green-200"}`}>{row.departure}</p>
                    </div>

                </div>
            </div>
        </>
    );
}

export default TimeboardRowComp;