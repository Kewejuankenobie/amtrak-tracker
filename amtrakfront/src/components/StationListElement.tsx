import {StationListEl} from "../types.ts";

function StationListElement({station, onClick}: {station: StationListEl, onClick: React.MouseEventHandler<HTMLDivElement>}) {


    return (
        <>
            <div className="bg-main-bg hover:bg-cyan-50 active:bg-blue-50 w-3/5 my-2 p-2 rounded-lg
            border-1 shadow-md
            cursor-pointer"
            onClick={onClick}>
                <p>{station.code}: {station.name}</p>
            </div>

        </>
    );
}

export default StationListElement;