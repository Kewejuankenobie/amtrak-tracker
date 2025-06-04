import {Link} from "react-router-dom";

function Home() {
    //The landing page of the train tracker, you can navigate to different features and read the terms of service

    return (
        <>
            {/*Main landing page html*/}
            <div className={`flex flex-row w-full justify-center items-center bg-main-bg lg:h-[95vh] overflow-y-scroll`}>
                <div className={'lg:w-3/5 w-4/5'}>
                    <h1 className={'font-bold text-6xl pb-20 lg:pt-0 pt-10'}>No More Train Anxiety</h1>
                    <p className={'text-5xl'}>
                        Simplify the process of tracking your Amtrak and Via Rail trains
                    </p>
                    <div className={'lg:grid grid-cols-2 lg:space-x-4 flex flex-col mt-10 mb-15'}>
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

                    {/*Terms of service html*/}
                    <div className={`text-gray-500 text-sm`}>
                        <p className={`text-md mb-2 font-semibold`}>Terms of Service:</p>
                        <p>By using this site, you agree to these terms of service</p>
                        <p className={`my-1 font-semibold`}>You Will Use This Site Knowing:</p>
                        <ol className={`list-decimal list-inside`}>
                            <li>This site has no affiliation with Amtrak or Via Rail</li>
                            <li>Realtime data is only updated every 2 minutes</li>
                            <li>You will contact the railroad or station staff for more up to date
                                information
                            </li>
                            <li>I claim no responsibility to any travel mishaps including delays or missing trains</li>
                        </ol>
                        <p className={`mt-2 mb-1 font-semibold`}>The AMV Train Tracker Uses Google Maps Features, And Their Usage Is
                            Subject To:</p>
                        <ol className={`list-decimal list-inside underline`}>
                            <li className={'list-item'}>
                                <a href={`https://maps.google.com/help/terms_maps/`}>Google Maps/Google Earth Terms of
                                    Service</a>
                            </li>
                            <li className={'list-item'}>
                                <a href={`https://policies.google.com/privacy`}>Google's Privacy Policy</a>
                            </li>
                        </ol>
                    </div>
                </div>
            </div>
        </>
    );
}

export default Home;