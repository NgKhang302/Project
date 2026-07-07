import { Link } from "react-router-dom";

const TYPE_LABELS = {
    READING: "Reading",
    WRITING: "Writing",
    LISTENING: "Listening",
};

export default function LessonCard({ lesson, completed = false }) {
    return (
        <Link to={`/lessons/${lesson.id}`} className="lesson-card">
            <div className="lesson-card-header">
                <span className={`lesson-card-badge badge-${lesson.contentType?.toLowerCase()}`}>
                    {TYPE_LABELS[lesson.contentType] || lesson.contentType}
                </span>
                {completed && <span className="lesson-card-check" title="Completed">✓</span>}
            </div>
            <h3 className="lesson-card-title">{lesson.title}</h3>
            {lesson.categoryName && <p className="lesson-card-category">{lesson.categoryName}</p>}
        </Link>
    );
}
