import Home from "./pages/Home.tsx";
import {APIProvider} from "@vis.gl/react-google-maps";
import 'dotenv'
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Closest from "./pages/Closest.tsx";
import Navbar from "./components/Navbar.tsx";

function App() {

    //Move to the backend, this is unsafe
    const key: string = import.meta.env.VITE_API_KEY;

  return (
    <>
      <div>
          <APIProvider apiKey={key} onLoad={() => console.log("Maps API Loaded")}>
              <BrowserRouter>
                  <Navbar />
                  <Routes>
                      <Route index element={<Home />} />
                      <Route path="closest" element={<Closest />} />
                  </Routes>
              </BrowserRouter>
          </APIProvider>
      </div>
    </>
  )
}

export default App
