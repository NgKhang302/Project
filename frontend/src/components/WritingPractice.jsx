import { useEffect, useState } from "react";
import { api } from "../api/api";

export default function WritingPractice({ lessonId, onSubmitted }) {
    const [content, setContent] = useState("");
    const [history, setHistory] = useState([]);
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        api.getWritingHistory(lessonId)
            .then(setHistory)
            .catch((err) => setError(err.message))
            .finally(() => setLoading(false));
    }, [lessonId]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!content.trim()) return;
        setSubmitting(true);
        setError(null);
        try {
            const result = await api.submitWriting(lessonId, content);
            setHistory((prev) => [result, ...prev]);
            setContent("");
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
                            <p className="writing-entry-content">{item.content}</p>
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
