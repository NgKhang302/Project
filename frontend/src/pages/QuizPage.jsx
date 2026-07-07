import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { api } from "../api/api";
import QuizQuestion from "../components/QuizQuestion";
import Alert from "../components/Alert";

export default function QuizPage() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [quizzes, setQuizzes] = useState([]);
    const [answers, setAnswers] = useState({});
    const [results, setResults] = useState({});
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);

    useEffect(() => {
        api.getQuizzesByLesson(id)
            .then(setQuizzes)
            .catch((err) => setError(err.message))
            .finally(() => setLoading(false));
    }, [id]);

    const handleSelect = (quizId, option) => {
        setAnswers((prev) => ({ ...prev, [quizId]: option }));
    };

    const handleSubmit = async () => {
        setSubmitting(true);
        setError(null);
        try {
            const entries = await Promise.all(
                quizzes.map(async (quiz) => {
                    const answer = answers[quiz.id];
                    if (!answer) return [quiz.id, null];
                    const result = await api.submitQuizAnswer(quiz.id, answer);
                    return [quiz.id, result];
                })
            );
            const resultMap = Object.fromEntries(entries.filter(([, result]) => result));
            setResults(resultMap);

            const correctCount = Object.values(resultMap).filter((r) => r.correct).length;
            navigate(`/lessons/${id}/quiz/result`, {
                state: { total: quizzes.length, correct: correctCount },
            });
        } catch (err) {
            setError(err.message);
        } finally {
            setSubmitting(false);
        }
    };

    if (loading) return <div className="page container">Loading quiz...</div>;

    const allAnswered = quizzes.length > 0 && quizzes.every((q) => answers[q.id]);

    return (
        <div className="page container">
            <h1>Quiz</h1>
            <Alert type="error">{error}</Alert>

            {quizzes.map((quiz, index) => (
                <QuizQuestion
                    key={quiz.id}
                    quiz={quiz}
                    index={index}
                    selectedAnswer={answers[quiz.id]}
                    onSelect={handleSelect}
                    result={results[quiz.id]}
                />
            ))}

            {quizzes.length === 0 && <p>This lesson has no quiz yet.</p>}

            {quizzes.length > 0 && (
                <button onClick={handleSubmit} disabled={!allAnswered || submitting}>
                    {submitting ? "Submitting..." : "Submit answers"}
                </button>
            )}
        </div>
    );
}
