import { useEffect, useState } from "react";

function stripHtml(html) {
    const div = document.createElement("div");
    div.innerHTML = html || "";
    return div.textContent || div.innerText || "";
}

export default function ListenButton({ text, label = "🔊 Nghe bằng giọng đọc" }) {
    const [speaking, setSpeaking] = useState(false);
    const supported = typeof window !== "undefined" && "speechSynthesis" in window;

    useEffect(() => {
        return () => {
            if (supported) window.speechSynthesis.cancel();
        };
    }, [supported]);

    if (!supported) return null;

    const handleToggle = () => {
        if (speaking) {
            window.speechSynthesis.cancel();
            setSpeaking(false);
            return;
        }

        const utterance = new SpeechSynthesisUtterance(stripHtml(text));
        utterance.lang = "en-US";
        utterance.rate = 0.95;
        utterance.onend = () => setSpeaking(false);
        utterance.onerror = () => setSpeaking(false);

        window.speechSynthesis.cancel();
        window.speechSynthesis.speak(utterance);
        setSpeaking(true);
    };

    return (
        <button type="button" className="secondary" onClick={handleToggle}>
            {speaking ? "⏹ Dừng" : label}
        </button>
    );
}
