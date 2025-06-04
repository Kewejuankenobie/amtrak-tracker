import {StationListEl} from "../types.ts";

function StationListElement({station, onClick}: {station: StationListEl, onClick: React.MouseEventHandler<HTMLDivElement>}) {
    //Component that gives a brief summary of a train station

    return (
        <>
            <div className={`bg-[#E0F2E1] ${station.code.length == 3 ? 'hover:bg-[#E8EDF0]' : 'hover:bg-[#EEF0E8]'} 
            active:bg-blue-50 md:w-3/5 w-4/5 my-4 p-6 rounded-lg
            shadow-md hover:-translate-y-1 hover:drop-shadow-lg duration-75
            cursor-pointer`}
            onClick={onClick}>
                <p className={`font-semibold text-lg mb-1.5`}>{station.code}: {station.name}, {station.admin_area}</p>
                {
                    station.code.length == 3 ?
                        <p className={`text-gray-700`}>Amtrak Station</p> :
                        <p className={`text-gray-700`}>VIA Rail Station</p>
                }
            </div>

        </>
    );
}

export default StationListElement;