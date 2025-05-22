import {Link, useLocation} from "react-router-dom";
import {useEffect, useState} from "react";
import { RiMenuLine } from "react-icons/ri";
import VerticalNav from "./VerticalNav.tsx";

function Navbar() {

    const path: string = useLocation().pathname;

    const [page, setPage] = useState<string>(path);
    const [openSidebar, setOpenSidebar] = useState<boolean>(false);

    useEffect(() => {setPage(path)}, [path])

    const checkSize = ( ) => {
        if (window.innerWidth >= 768) {
            setOpenSidebar(false);
        }
    }

    addEventListener("resize", checkSize);

    return (
        <>
            <div
                className="flex md:h-[5vh] h-[15vh] items-center justify-center w-full border-b-1 border-gray-200 bg-main-bg">
                {openSidebar && <VerticalNav onClick={() => setOpenSidebar(false)} delayClick={() =>
                    setTimeout(() => {
                        setOpenSidebar(false);
                }, 300)} />}
                <div className="flex flex-row items-center justify-between md:w-3/5 w-4/5">
                    <Link className="font-bold text-2xl py-2 text-green-900 cursor-pointer" to="/">
                        <div className="flex md:items-start items-center justify-normal w-full text-pretty">
                            <img src="../../logo.svg" alt="Logo Not Found" className="md:w-10 w-14 mr-0.5"/>
                            <p className="md:visible collapse">AMV Train Tracker</p>
                        </div>
                    </Link>
                    <div className="md:visible collapse">
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
                <div className="md:hidden visible">
                    {
                        <button className={`text-3xl cursor-pointer ${openSidebar && "animate-men"}`}
                            onClick={() => setOpenSidebar(!openSidebar)}>
                            <RiMenuLine onClick={() => setOpenSidebar(false)}/>
                        </button>
                    }
                </div>
            </div>
        </>
    );
}

export default Navbar;