import { useEffect, useRef, useState } from "react";
import { api } from "../api/api";
import GrammarFeedback from "./GrammarFeedback";

export default function WritingPractice({ lessonId, onSubmitted }) {
    const [content, setContent] = useState("");
    const [liveErrors, setLiveErrors] = useState([]);
    const [checking, setChecking] = useState(false);
    const [history, setHistory] = useState([]);
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const debounceRef = useRef(null);

    useEffect(() => {
        api.getWritingHistory(lessonId)
            .then(setHistory)
            .catch((err) => setError(err.message))
            .finally(() => setLoading(false));
    }, [lessonId]);

    useEffect(() => {
        clearTimeout(debounceRef.current);
        if (!content.trim()) {
            setLiveErrors([]);
            return;
        }
        debounceRef.current = setTimeout(() => {
            setChecking(true);
            api.checkWriting(lessonId, content)
                .then((result) => setLiveErrors(result.matches || []))
                .catch(() => {})
                .finally(() => setChecking(false));
        }, 1200);
        return () => clearTimeout(debounceRef.current);
    }, [content, lessonId]);

    const applyReplacement = (offset, length, replacement) => {
        setContent((prev) => prev.slice(0, offset) + replacement + prev.slice(offset + length));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!content.trim()) return;
        setSubmitting(true);
        setError(null);
        try {
            const result = await api.submitWriting(lessonId, content);
            setHistory((prev) => [result, ...prev]);
            setContent("");
            setLiveErrors([]);
            onSubmitted?.();
        } catch (err) {
            setError(err.message);
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div className="writing-practice">
            <h3>Bài viết của bạn</h3>
            <form className="form" onSubmit={handleSubmit}>
                <textarea
                    placeholder="Viết câu trả lời của bạn ở đây..."
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    rows={6}
                    required
                />
                {content.trim() && (
                    <div className="grammar-live-preview">
                        {checking && liveErrors.length === 0 && <p className="grammar-checking">Đang kiểm tra ngữ pháp...</p>}
                        <GrammarFeedback content={content} errors={liveErrors} onApply={applyReplacement} />
                        {!checking && liveErrors.length > 0 && (
                            <p className="grammar-live-count">⚠ {liveErrors.length} lỗi được phát hiện — nhấn vào từ được gạch chân để xem gợi ý.</p>
                        )}
                    </div>
                )}
                <button type="submit" disabled={submitting}>
                    {submitting ? "Đang nộp..." : "Nộp bài"}
                </button>
            </form>

            {error && <p className="alert alert-error">{error}</p>}

            {!loading && history.length > 0 && (
                <div className="writing-history">
                    <h4>Lịch sử bài đã nộp</h4>
                    {history.map((item) => (
                        <div key={item.id} className="writing-entry">
                            <GrammarFeedback content={item.content} errors={item.errors || []} />
                            <p className="writing-entry-feedback">💬 {item.feedback}</p>
                            <span className="writing-entry-date">
                                {new Date(item.submittedAt).toLocaleString("vi-VN")}
                            </span>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}
