import { useEffect, useRef } from "react";

export default function DialogueTranscript({ lines, activeIndex }) {
    const activeRef = useRef(null);

    useEffect(() => {
        activeRef.current?.scrollIntoView({ block: "nearest", behavior: "smooth" });
    }, [activeIndex]);

    return (
        <div className="dialogue-transcript">
            {lines.map((line, i) => (
                <p
                    key={line.id ?? i}
                    ref={i === activeIndex ? activeRef : null}
                    className={i === activeIndex ? "dialogue-line active" : "dialogue-line"}
                >
                    <strong>{line.speaker}:</strong> {line.text}
                </p>
            ))}
        </div>
    );
}
