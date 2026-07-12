import { useEffect, useRef, useState } from "react";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api";

function stripHtml(html) {
    const div = document.createElement("div");
    div.innerHTML = html || "";
    return div.textContent || div.innerText || "";
}

function splitIntoChunks(text, maxLen = 200) {
    const sentences = text.match(/[^.!?]+[.!?]*\s*/g) || [text];
    const chunks = [];
    let current = "";
    for (const sentence of sentences) {
        if (current && (current + sentence).length > maxLen) {
            chunks.push(current.trim());
            current = sentence;
        } else {
            current += sentence;
        }
    }
    if (current.trim()) chunks.push(current.trim());
    return chunks.filter(Boolean);
}

function googleTtsUrl(chunk) {
    return `${API_BASE_URL}/public/tts?lang=en&text=${encodeURIComponent(chunk)}`;
}

function chunksFromLines(lines, maxLen = 200) {
    const out = [];
    lines.forEach((line, lineIndex) => {
        const text = (line.text || "").trim();
        if (!text) return;
        if (text.length <= maxLen) {
            out.push({ lineIndex, text });
        } else {
            for (const sub of splitIntoChunks(text, maxLen)) {
                out.push({ lineIndex, text: sub });
            }
        }
    });
    return out;
}

export default function ListenButton({ text, label = "🔊 Nghe bằng giọng đọc", lines, onLineStart }) {
    const [speaking, setSpeaking] = useState(false);
    const audioRef = useRef(null);
    const pendingRef = useRef(null);
    const stopRef = useRef(false);

    useEffect(() => {
        return () => {
            stopRef.current = true;
            audioRef.current?.pause();
            pendingRef.current?.reject(new Error("stopped"));
            if (typeof window !== "undefined" && "speechSynthesis" in window) {
                window.speechSynthesis.cancel();
            }
        };
    }, []);

    const playChunks = async (chunks) => {
        for (const chunk of chunks) {
            if (stopRef.current) return true;
            onLineStart?.(chunk.lineIndex);
            const audio = new Audio(googleTtsUrl(chunk.text));
            audio.playbackRate = 0.85;
            audioRef.current = audio;
            try {
                await new Promise((resolve, reject) => {
                    pendingRef.current = { resolve, reject };
                    audio.onended = resolve;
                    audio.onerror = () => reject(new Error("tts-failed"));
                    audio.play().catch(reject);
                });
            } catch (err) {
                pendingRef.current = null;
                return err?.message === "stopped";
            }
            pendingRef.current = null;
        }
        return true;
    };

    const speakWithBrowserVoice = (plain) => {
        if (!(typeof window !== "undefined" && "speechSynthesis" in window)) {
            setSpeaking(false);
            return;
        }
        const utterance = new SpeechSynthesisUtterance(plain);
        utterance.lang = "en-US";
        utterance.rate = 0.85;
        utterance.pitch = 1;
        utterance.onend = () => {
            onLineStart?.(-1);
            setSpeaking(false);
        };
        utterance.onerror = () => {
            onLineStart?.(-1);
            setSpeaking(false);
        };
        window.speechSynthesis.speak(utterance);
    };

    const stopAll = () => {
        stopRef.current = true;
        audioRef.current?.pause();
        pendingRef.current?.reject(new Error("stopped"));
        if (typeof window !== "undefined" && "speechSynthesis" in window) {
            window.speechSynthesis.cancel();
        }
        onLineStart?.(-1);
    };

    const handleToggle = async () => {
        if (speaking) {
            stopAll();
            setSpeaking(false);
            return;
        }

        const hasLines = lines?.length > 0;
        const plain = hasLines ? lines.map((l) => l.text).join(". ") : stripHtml(text).trim();
        if (!plain.trim()) return;

        const chunks = hasLines ? chunksFromLines(lines) : splitIntoChunks(plain).map((c) => ({ lineIndex: -1, text: c }));
        if (chunks.length === 0) return;

        stopRef.current = false;
        setSpeaking(true);

        const ok = await playChunks(chunks);
        if (stopRef.current) return;

        if (!ok) {
            speakWithBrowserVoice(plain);
            return;
        }
        onLineStart?.(-1);
        setSpeaking(false);
    };

    return (
        <button type="button" className="secondary" onClick={handleToggle}>
            {speaking ? "⏹ Dừng" : label}
        </button>
    );
}
