export const getTrainUpdates = async () => {
    const response = await fetch(`http://localhost:8080/train/getAll`);
    const data = await response.json();
    return data.data;
}