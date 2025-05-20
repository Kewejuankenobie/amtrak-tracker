import {Link} from "react-router-dom";

function Home() {
    return (
        <>
            <div className={`flex flex-row w-full justify-center items-center bg-main-bg lg:h-[95vh]`}>
                <div className={'w-3/5'}>
                    <h1 className={'font-bold text-6xl pb-20 lg:pt-0 pt-10'}>No More Train Anxiety</h1>
                    <p className={'text-5xl'}>
                        Simplify the process of tracking your Amtrak and Via Rail trains
                    </p>
                    <div className={'lg:grid grid-cols-2 lg:space-x-4 flex flex-col my-10'}>
                        <Link to="/train-info">
                            <div className={'px-2 pt-20 pb-14 my-2 bg-[#E0F2E1] rounded-lg cursor-pointer shadow-md ' +
                                'hover:bg-[#E8F0E8] active:bg-green-50 hover:-translate-y-1 hover:drop-shadow-lg duration-75'}>
                                <h2 className={`font-bold text-2xl`}>Track Your Train's Information</h2>
                                <p className={`text-gray-700`}>Learn more about how late your train is, where it is, and how fast it is moving</p>
                            </div>
                            </Link>
                        <Link to="/station-info">
                            <div className={'px-2 my-2 py-20 bg-[#E0F2E1] rounded-lg cursor-pointer shadow-md ' +
                                'hover:bg-[#E8F0E8] active:bg-green-50 hover:-translate-y-1 hover:drop-shadow-lg duration-75'}>
                                <h2 className={`font-bold text-2xl`}>Look Up A Station's Time Tables</h2>
                                <p className={`text-gray-700`}>See when each train arrives and leaves the station</p>
                            </div>
                        </Link>
                    </div>

                    <div className={`text-gray-500 text-sm`}>
                        <p>This site has no affiliation with Amtrak or Via Rail. Realtime Data is only updated every 2
                            minutes</p>
                        <p>To ensure you don't miss your train, contact the railroad or station staff for more
                            information, plus arrive to the station early</p>
                        <p>I claim no responsibility to any travel mishaps</p>
                    </div>
                </div>
            </div>
        </>
    );
}

export default Home;