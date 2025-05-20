import {Link} from "react-router-dom";
import { IoMdClose } from "react-icons/io";

function VerticalNav({onClick}: {onClick: React.MouseEventHandler<SVGElement | HTMLAnchorElement>}) {

    return (
        <>
            <div className={`flex flex-col w-full absolute left-0 top-0 text-2xl object-top-left z-999 bg-white
             border-b-1 pb-10 border-gray-200 shadow-4xl`}>
                <div className="flex justify-end w-full text-4xl">
                    <IoMdClose className="cursor-pointer hover:text-green-400 my-[5vh] mr-[8vw]"
                               onClick={onClick} />
                </div>
                <Link className={`mr-8 cursor-pointer w-full
                        hover:text-primary-link hover:text-primary-link hover:bg-green-50 transition delay-75
                         active:bg-green-50 p-4`}
                      to="/"
                    onClick={onClick}>
                    Home
                </Link>
                <Link className={`mr-8 cursor-pointer w-full
                        hover:text-primary-link hover:text-primary-link hover:bg-green-50 transition delay-75
                         active:bg-green-50 p-4`}
                      to="/train-info"
                      onClick={onClick}>
                    Trains
                </Link>
                <Link className={`cursor-pointer w-full
                        hover:text-primary-link hover:text-primary-link hover:bg-green-50 transition delay-75
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