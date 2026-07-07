export default function QuizQuestion({ quiz, index, selectedAnswer, onSelect, result }) {
    return (
        <div className="quiz-question">
            <h3 className="quiz-question-text">
                {index + 1}. {quiz.question}
            </h3>
            <div className="quiz-options">
                {quiz.options.map((option) => {
                    const isSelected = selectedAnswer === option;
                    const isCorrect = result && option === result.correctAnswer;
                    const isWrongSelected = result && isSelected && !result.correct;

                    let className = "quiz-option";
                    if (isSelected) className += " selected";
                    if (result && isCorrect) className += " correct";
                    if (isWrongSelected) className += " incorrect";

                    return (
                        <button
                            key={option}
                            type="button"
                            className={className}
                            disabled={!!result}
                            onClick={() => onSelect(quiz.id, option)}
                        >
                            {option}
                        </button>
                    );
                })}
            </div>
            {result && result.explanation && (
                <p className="quiz-explanation">{result.explanation}</p>
            )}
        </div>
    );
}
