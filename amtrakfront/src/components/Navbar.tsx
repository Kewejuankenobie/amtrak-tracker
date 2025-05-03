import {Link} from "react-router-dom";

function Navbar() {

    return (
        <>
            <div
                className="flex lg:flex-row flex-col items-center justify-center lg:justify-between w-full border-b-1 border-gray-200 bg-gray-100">
                <Link className="font-bold text-2xl py-2 ml-10 text-blue-900 cursor-pointer" to="/">Amtrak Tracker</Link>
                <div>
                    <Link className="border-b-2 mr-10 cursor-pointer
                    hover:border-blue-700 hover:text-blue-700 hover:border-b-3 transition delay-75"
                        to="/">
                        All
                    </Link>
                    <Link className="border-b-2 mr-10 cursor-pointer
                    hover:border-blue-700 hover:text-blue-700 hover:border-b-3 transition delay-75"
                        to="/closest">
                        Closest Five
                    </Link>
                </div>
            </div>
        </>
    );
}

export default Navbar;