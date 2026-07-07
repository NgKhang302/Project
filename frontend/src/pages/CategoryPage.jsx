import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { api } from "../api/api";
import { useAuth } from "../hooks/useAuth";
import LessonCard from "../components/LessonCard";
import Alert from "../components/Alert";

const TYPES = [
    { value: "", label: "All" },
    { value: "READING", label: "Reading" },
    { value: "WRITING", label: "Writing" },
    { value: "LISTENING", label: "Listening" },
];

export default function CategoryPage() {
    const { id } = useParams();
    const { isAuthenticated } = useAuth();
    const [lessons, setLessons] = useState([]);
    const [completedIds, setCompletedIds] = useState(new Set());
    const [contentType, setContentType] = useState("");
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        setLoading(true);
        Promise.all([
            api.getLessonsByCategory(id, contentType || undefined),
            isAuthenticated ? api.getUserProgress().catch(() => []) : Promise.resolve([]),
        ])
            .then(([lessonData, progress]) => {
                setLessons(lessonData);
                setCompletedIds(new Set(progress.filter((p) => p.status === "COMPLETED").map((p) => p.lessonId)));
            })
            .catch((err) => setError(err.message))
            .finally(() => setLoading(false));
    }, [id, contentType, isAuthenticated]);

    return (
        <div className="page container">
            <h1>Lessons</h1>
            <div className="filter-tabs">
                {TYPES.map((type) => (
                    <button
                        key={type.value}
                        className={contentType === type.value ? "filter-tab active" : "filter-tab"}
                        onClick={() => setContentType(type.value)}
                    >
                        {type.label}
                    </button>
                ))}
            </div>

            <Alert type="error">{error}</Alert>

            {loading ? (
                <p>Loading lessons...</p>
            ) : (
                <div className="lesson-grid">
                    {lessons.map((lesson) => (
                        <LessonCard key={lesson.id} lesson={lesson} completed={completedIds.has(lesson.id)} />
                    ))}
                    {lessons.length === 0 && <p>No lessons in this category yet.</p>}
                </div>
            )}
        </div>
    );
}
