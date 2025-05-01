import Home from "./pages/Home.tsx";
import {APIProvider} from "@vis.gl/react-google-maps";
import 'dotenv'

function App() {

    //Move to the backend, this is unsafe
    const key: string = import.meta.env.VITE_API_KEY;

  return (
    <>
      <div>
          <APIProvider apiKey={key} onLoad={() => console.log("Maps API Loaded")}>
              <Home />
          </APIProvider>
      </div>
    </>
  )
}

export default App
