const BASE_URL: string = 'http://localhost:8080/train';

export const getTrainUpdates = async () => {
    const response = await fetch(`${BASE_URL}/getAll`);
    const data = await response.json();
    return data.data;
}

export const searchTrain = async (searchTerm: string) => {
    if (searchTerm === '') {
        return await getTrainUpdates();
    }
    const response = await fetch(`${BASE_URL}/search/${encodeURIComponent(searchTerm)}`);
    const data = await response.json();
    return data.data;
}

export const getClosestTrains = async(position: GeolocationPosition) => {
    const response = await fetch(`${BASE_URL}/closest/${encodeURIComponent(position.coords.latitude)}
    /${encodeURIComponent(position.coords.longitude)}`);
    const data = await response.json();
    return data.data;
}