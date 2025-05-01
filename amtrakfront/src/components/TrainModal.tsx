
const TrainModal = ({open, onClose}) => {

    if (!open) {
        return null;
    }

    return (
        <>
            <div className={`fixed inset-0 justify-center items-center transition-colors bg-black/20`}>
                <div className="flex flex-col justify-left bg-white">
                    <button className="border-1" onClick={() => onClose(false)}>X</button>
                    <h1>Extra Information</h1>
                </div>
            </div>
        </>
    )
}

export default TrainModal;