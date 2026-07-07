import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { api } from "../api/api";
import LessonCard from "../components/LessonCard";
import Alert from "../components/Alert";

export default function SearchResults() {
    const [searchParams] = useSearchParams();
    const q = searchParams.get("q") || "";
    const [lessons, setLessons] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (!q.trim()) {
            setLessons([]);
            setLoading(false);
            return;
        }
        setLoading(true);
        api.searchLessons(q)
            .then(setLessons)
            .catch((err) => setError(err.message))
            .finally(() => setLoading(false));
    }, [q]);

    return (
        <div className="page container">
            <h1>Kết quả tìm kiếm cho "{q}"</h1>
            <Alert type="error">{error}</Alert>

            {loading ? (
                <p>Đang tìm kiếm...</p>
            ) : (
                <div className="lesson-grid">
                    {lessons.map((lesson) => (
                        <LessonCard key={lesson.id} lesson={lesson} />
                    ))}
                    {lessons.length === 0 && <p>Không tìm thấy bài học nào phù hợp.</p>}
                </div>
            )}
        </div>
    );
}
