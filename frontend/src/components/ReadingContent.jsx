import { useState } from "react";
import { wordifyHtml } from "../utils/wordify";
import DictionaryPopup from "./DictionaryPopup";

export default function ReadingContent({ html }) {
    const [popup, setPopup] = useState(null);

    const handleClick = (e) => {
        const word = e.target.dataset?.word;
        if (!word) return;
        setPopup({ word, x: e.clientX, y: e.clientY + 12 });
    };

    return (
        <>
            <div
                className="lesson-content"
                onClick={handleClick}
                dangerouslySetInnerHTML={{ __html: wordifyHtml(html || "") }}
            />
            {popup && (
                <DictionaryPopup
                    word={popup.word}
                    x={popup.x}
                    y={popup.y}
                    onClose={() => setPopup(null)}
                />
            )}
        </>
    );
}
