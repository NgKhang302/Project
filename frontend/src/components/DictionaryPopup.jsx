import { useEffect, useState } from "react";
import { api } from "../api/api";

export default function DictionaryPopup({ word, x, y, onClose }) {
    const [data, setData] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        setLoading(true);
        setError(null);
        setData(null);
        api.lookupWord(word)
            .then(setData)
            .catch((err) => setError(err.message || "Không tìm thấy nghĩa của từ này."))
            .finally(() => setLoading(false));
    }, [word]);

    return (
        <div className="popover dictionary-popup" style={{ left: x, top: y }}>
            <button type="button" className="popover-close" onClick={onClose} aria-label="Close">
                ×
            </button>
            <div className="dictionary-popup-word">
                {word}
                {data?.phoneticAudioUrl && (
                    <button
                        type="button"
                        className="link-button"
                        onClick={() => new Audio(data.phoneticAudioUrl).play()}
                    >
                        🔊
                    </button>
                )}
            </div>
            {data?.phonetic && <div className="dictionary-popup-phonetic">{data.phonetic}</div>}
            {data?.vietnameseTranslation && (
                <div className="dictionary-popup-translation">🇻🇳 {data.vietnameseTranslation}</div>
            )}

            {loading && <p>Đang tra từ...</p>}
            {error && <p className="dictionary-popup-error">{error}</p>}

            {data?.meanings?.map((meaning, i) => (
                <div key={i} className="dictionary-popup-meaning">
                    <strong>{meaning.partOfSpeech}</strong>
                    <ol>
                        {meaning.definitions?.slice(0, 3).map((def, j) => (
                            <li key={j}>
                                {def.definition}
                                {def.example && <div className="dictionary-popup-example">"{def.example}"</div>}
                            </li>
                        ))}
                    </ol>
                </div>
            ))}
        </div>
    );
}
