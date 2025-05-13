import {Link} from "react-router-dom";
import {useState} from "react";

function Navbar() {

    const [page, setPage] = useState<number>(0);

    return (
        <>
            <div
                className="flex lg:h-[5vh] h-[10vh] items-center justify-center w-full border-b-1 border-gray-200 bg-main-bg">
                <div className="flex lg:flex-row flex-col items-center justify-between w-3/5">
                    <Link className="font-bold text-2xl py-2 text-green-900 cursor-pointer" to="/"
                    onClick={() => setPage(0)}>
                        <div className="flex items-start justify-normal w-full">
                            <img src="../../public/logo.svg" alt="Logo Not Found" className="w-10 mr-0.5"/>
                            <p>AMV Train Tracker</p>
                        </div>
                    </Link>
                    <div>
                        <Link className={`mr-8 cursor-pointer
                        ${
                            page == 0 && "text-primary-link"
                        }
                        hover:text-primary-link hover:text-primary-link transition delay-75`}
                            to="/"
                        onClick={() => setPage(0)}>
                            Trains
                        </Link>
                        <Link className={`mr-8 cursor-pointer
                        ${
                            page == 1 && "text-primary-link"
                        }
                        hover:text-primary-link hover:text-primary-link transition delay-75`}
                            to="/closest"
                        onClick={() => setPage(1)}>
                            Closest Five
                        </Link>
                        <Link className={`cursor-pointer
                        ${
                            page == 2 && "text-primary-link"
                        }
                        hover:text-primary-link hover:text-primary-link transition delay-75`}
                              to="/station-info"
                        onClick={() => setPage(2)}>
                            Stations
                        </Link>
                    </div>
                </div>
            </div>
        </>
    );
}

export default Navbar;