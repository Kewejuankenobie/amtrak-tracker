const BASE_URL: string = import.meta.env.VITE_DB_LINK + 'train';

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