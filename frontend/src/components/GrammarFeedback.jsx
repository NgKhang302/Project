import { useState } from "react";

function clipOverlaps(errors) {
    const sorted = [...errors]
        .filter((m) => m.length > 0)
        .sort((a, b) => a.offset - b.offset);
    const clipped = [];
    let cursor = 0;
    for (const match of sorted) {
        if (match.offset < cursor) continue;
        clipped.push(match);
        cursor = match.offset + match.length;
    }
    return clipped;
}

function buildSegments(content, errors) {
    const clipped = clipOverlaps(errors);
    const segments = [];
    let pos = 0;
    clipped.forEach((match, i) => {
        if (match.offset > pos) {
            segments.push({ type: "text", text: content.slice(pos, match.offset), key: `t${i}` });
        }
        segments.push({
            type: "error",
            text: content.slice(match.offset, match.offset + match.length),
            match,
            key: `e${i}`,
        });
        pos = match.offset + match.length;
    });
    if (pos < content.length) {
        segments.push({ type: "text", text: content.slice(pos), key: "t-last" });
    }
    return segments;
}

export default function GrammarFeedback({ content, errors = [], onApply }) {
    const [active, setActive] = useState(null);

    if (!content) return null;
    const segments = buildSegments(content, errors);

    return (
        <div className="grammar-feedback">
            {segments.map((seg) =>
                seg.type === "text" ? (
                    <span key={seg.key}>{seg.text}</span>
                ) : (
                    <mark
                        key={seg.key}
                        className="grammar-error"
                        onClick={(e) => setActive({ match: seg.match, x: e.clientX, y: e.clientY + 12 })}
                    >
                        {seg.text}
                    </mark>
                )
            )}
            {active && (
                <div className="popover grammar-popover" style={{ left: active.x, top: active.y }}>
                    <button type="button" className="popover-close" onClick={() => setActive(null)} aria-label="Close">
                        ×
                    </button>
                    <p>{active.match.shortMessage || active.match.message}</p>
                    {active.match.replacements?.length > 0 && (
                        <div className="replacement-chips">
                            {active.match.replacements.map((replacement) => (
                                <button
                                    key={replacement}
                                    type="button"
                                    className="replacement-chip"
                                    onClick={() => {
                                        onApply?.(active.match.offset, active.match.length, replacement);
                                        setActive(null);
                                    }}
                                >
                                    {replacement}
                                </button>
                            ))}
                        </div>
                    )}
                </div>
            )}
        </div>
    );
}
