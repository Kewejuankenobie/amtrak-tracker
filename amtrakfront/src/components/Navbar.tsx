import {Link, useLocation} from "react-router-dom";
import {useEffect, useState} from "react";

function Navbar() {

    const path = useLocation().pathname;

    const [page, setPage] = useState<string>(path);

    useEffect(() => {setPage(path)}, [path])


    return (
        <>
            <div
                className="flex lg:h-[5vh] h-[10vh] items-center justify-center w-full border-b-1 border-gray-200 bg-main-bg">
                <div className="flex lg:flex-row flex-col items-center justify-between w-3/5">
                    <Link className="font-bold text-2xl py-2 text-green-900 cursor-pointer" to="/">
                        <div className="flex items-start justify-normal w-full">
                            <img src="../../public/logo.svg" alt="Logo Not Found" className="w-10 mr-0.5"/>
                            <p>AMV Train Tracker</p>
                        </div>
                    </Link>
                    <div>
                        <Link className={`mr-8 cursor-pointer
                        ${
                            page == '/' && "text-primary-link"
                        }
                        hover:text-primary-link hover:text-primary-link transition delay-75`}
                              to="/">
                            Home
                        </Link>
                        <Link className={`mr-8 cursor-pointer
                        ${
                            page == '/train-info' && "text-primary-link"
                        }
                        hover:text-primary-link hover:text-primary-link transition delay-75`}
                            to="/train-info">
                            Trains
                        </Link>
                        {/*<Link className={`mr-8 cursor-pointer*/}
                        {/*${*/}
                        {/*    page == '/closest' && "text-primary-link"*/}
                        {/*}*/}
                        {/*hover:text-primary-link hover:text-primary-link transition delay-75`}*/}
                        {/*    to="/closest"*/}
                        {/*onClick={() => setPage('/closest')}>*/}
                        {/*    Closest Five*/}
                        {/*</Link>*/}
                        <Link className={`cursor-pointer
                        ${
                            page == '/station-info' && "text-primary-link"
                        }
                        hover:text-primary-link hover:text-primary-link transition delay-75`}
                              to="/station-info">
                            Stations
                        </Link>
                    </div>
                </div>
            </div>
        </>
    );
}

export default Navbar;