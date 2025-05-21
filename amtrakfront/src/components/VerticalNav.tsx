import {Link} from "react-router-dom";
import { IoMdClose } from "react-icons/io";
import {useState} from "react";

function VerticalNav({onClick, delayClick}: {onClick: React.MouseEventHandler<SVGElement | HTMLAnchorElement>,
    delayClick: React.MouseEventHandler<SVGElement | HTMLAnchorElement>}) {

    const [clickX, setClickX] = useState<boolean>(false);

    return (
        <>
            <div className={`flex flex-col w-full absolute left-0 top-0 text-2xl object-top-left z-999 bg-white
             border-b-1 pb-10 border-gray-200 shadow-4xl open:bg-red-200 animate-dropdown ${clickX && "animate-dropup"}`}>
                <div className="flex justify-end w-full text-4xl">
                    <div className="my-[5vh] mr-[6vw] rounded-full hover:bg-green-50 active:bg-green-50"
                    onClick={() => setClickX(!clickX)}>
                        <IoMdClose className={`cursor-pointer hover:text-primary-link m-2 ${clickX && "animate-x"}`}
                                   onClick={delayClick} />
                    </div>
                </div>
                <Link className={`mr-8 cursor-pointer w-full
                        hover:text-primary-link hover:text-primary-link hover:bg-green-50 
                         active:bg-green-50 p-4`}
                      to="/"
                    onClick={onClick}>
                    Home
                </Link>
                <Link className={`mr-8 cursor-pointer w-full
                        hover:text-primary-link hover:text-primary-link hover:bg-green-50
                         active:bg-green-50 p-4`}
                      to="/train-info"
                      onClick={onClick}>
                    Trains
                </Link>
                <Link className={`cursor-pointer w-full
                        hover:text-primary-link hover:text-primary-link hover:bg-green-50
                         active:bg-green-50 p-4`}
                      to="/station-info"
                      onClick={onClick}>
                    Stations
                </Link>
            </div>
        </>
    );
}

export default VerticalNav;