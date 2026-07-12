import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { api } from "../api/api";
import { useAuth } from "../hooks/useAuth";
import Alert from "../components/Alert";
import WritingPractice from "../components/WritingPractice";
import ReadingContent from "../components/ReadingContent";
import ListeningContent from "../components/ListeningContent";

const TYPE_LABELS = {
    READING: "Reading",
    WRITING: "Writing",
    LISTENING: "Listening",
};

export default function LessonPage() {
    const { id } = useParams();
    const navigate = useNavigate();
    const { isAuthenticated } = useAuth();
    const [lesson, setLesson] = useState(null);
    const [quizCount, setQuizCount] = useState(0);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [completing, setCompleting] = useState(false);
    const [completed, setCompleted] = useState(false);

    useEffect(() => {
        setLoading(true);
        Promise.all([
            isAuthenticated ? api.getLesson(id) : api.getPublicLesson(id),
            api.getQuizzesByLesson(id).catch(() => []),
        ])
            .then(([lessonData, quizzes]) => {
                setLesson(lessonData);
                setQuizCount(quizzes.length);
            })
            .catch((err) => setError(err.message))
            .finally(() => setLoading(false));
    }, [id, isAuthenticated]);

    const handleComplete = async () => {
        setCompleting(true);
        try {
            await api.completeLesson(id);
            setCompleted(true);
        } catch (err) {
            setError(err.message);
        } finally {
            setCompleting(false);
        }
    };

    if (loading) return <div className="page container">Loading lesson...</div>;

    return (
        <div className="page container">
            <Alert type="error">{error}</Alert>
            {lesson && (
                <>
                    <span className={`lesson-card-badge badge-${lesson.contentType?.toLowerCase()}`}>
                        {TYPE_LABELS[lesson.contentType] || lesson.contentType}
                    </span>
                    {lesson.cefrLevel && <span className="cefr-badge">{lesson.cefrLevel}</span>}
                    <h1>{lesson.title}</h1>

                    {lesson.contentType === "LISTENING" && <ListeningContent lesson={lesson} />}

                    {lesson.contentType === "READING" ? (
                        <ReadingContent html={lesson.content} />
                    ) : (
                        <div className="lesson-content" dangerouslySetInnerHTML={{ __html: lesson.content || "" }} />
                    )}

                    {isAuthenticated && lesson.contentType === "WRITING" ? (
                        <WritingPractice lessonId={id} onSubmitted={() => setCompleted(true)} />
                    ) : (
                        <div className="lesson-actions">
                            {isAuthenticated && (
                                <button onClick={handleComplete} disabled={completing || completed}>
                                    {completed ? "Completed ✓" : completing ? "Saving..." : "Mark as complete"}
                                </button>
                            )}
                            {quizCount > 0 && (
                                <button className="secondary" onClick={() => navigate(`/lessons/${id}/quiz`)}>
                                    Take quiz ({quizCount} questions)
                                </button>
                            )}
                        </div>
                    )}
                </>
            )}
        </div>
    );
}
