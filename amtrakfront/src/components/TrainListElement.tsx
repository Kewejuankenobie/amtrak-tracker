function TrainListElement({train}) {
    return (
        <>
            <div className="bg-cyan-100 w-md my-2 p-2 rounded-lg border-1 border-blue-200 shadow-md">
                <p className="underline">Train {train.number}: {train.name}</p>
                <p>Destination: {train.destination}</p>
                <p>{train.speed} mph</p>
                {
                    train.last_station_dekay < 0 ?
                        <p>Left {train.last_station} {Math.round(-1 * train.last_station_delay / 60)} minutes late</p> :
                        <p>Left {train.last_station} on time</p>
                }
                {
                    train.next_station_delay < 0 ?
                        <p>Estimated arrival at {train.next_station} {Math.round(-1 * train.next_station_delay / 60)} minutes late</p> :
                        <p>Estimated arrival at {train.next_station} on time</p>
                }
            </div>

        </>

    );
}

export default TrainListElement;