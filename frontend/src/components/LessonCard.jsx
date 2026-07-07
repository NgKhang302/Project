import { Link } from "react-router-dom";

const TYPE_LABELS = {
    READING: "Reading",
    WRITING: "Writing",
    LISTENING: "Listening",
};

export default function LessonCard({ lesson }) {
    return (
        <Link to={`/lessons/${lesson.id}`} className="lesson-card">
            <span className={`lesson-card-badge badge-${lesson.contentType?.toLowerCase()}`}>
                {TYPE_LABELS[lesson.contentType] || lesson.contentType}
            </span>
            <h3 className="lesson-card-title">{lesson.title}</h3>
            {lesson.categoryName && <p className="lesson-card-category">{lesson.categoryName}</p>}
        </Link>
    );
}
