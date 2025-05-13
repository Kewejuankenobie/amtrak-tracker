const BASE_URL: string = import.meta.env.VITE_DB_LINK + 'station';

export const getAllStations = async () => {
    const response = await fetch(`${BASE_URL}/getAllStations`);
    const stations = await response.json();
    return stations.data;
}

export const searchStations = async (query: string) => {
    if (query == '') {
        return getAllStations();
    }
    const response = await fetch(`${BASE_URL}/search/${query}`);
    const data = await response.json();
    return data.data;
}

export const getTimeboard = async (stationId: string) => {
    const response = await fetch(`${BASE_URL}/get/${stationId}`);
    const data = await response.json();
    return data.data;
}