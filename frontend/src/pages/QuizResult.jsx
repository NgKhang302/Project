import { Link, useLocation, useParams } from "react-router-dom";

export default function QuizResult() {
    const { id } = useParams();
    const location = useLocation();
    const { total = 0, correct = 0 } = location.state || {};
    const percent = total > 0 ? Math.round((correct / total) * 100) : 0;

    return (
        <div className="page container narrow quiz-result">
            <h1>Quiz Result</h1>
            <div className="quiz-result-score">{percent}%</div>
            <p>
                You answered {correct} out of {total} questions correctly.
            </p>
            <div className="lesson-actions">
                <Link to={`/lessons/${id}`}>
                    <button className="secondary">Back to lesson</button>
                </Link>
                <Link to={`/lessons/${id}/quiz`}>
                    <button>Retry quiz</button>
                </Link>
            </div>
        </div>
    );
}
