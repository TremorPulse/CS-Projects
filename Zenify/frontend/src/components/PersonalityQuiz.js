import React, { useEffect, useState } from 'react';
import axios from 'axios';

function PersonalityQuiz() {
    const [questions, setQuestions] = useState([]);
    const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
    const [selectedAnswer, setSelectedAnswer] = useState('');

    const answerChoices = [
        "Slightly Disagree",
        "Disagree",
        "Neutral",
        "Slightly Agree",
        "Agree"
    ];

    useEffect(() => {
        axios.get('http://127.0.0.1:8000/dashboard/personality-quiz/')
            .then(res => {
                setQuestions(res.data);
            })
            .catch(err => console.log(err));
    }, []);

    return (
        <div>
            <h1>Personality Quiz</h1>
            {questions.length > 0 && (
                <div>
                    <h1>{questions[currentQuestionIndex].question}</h1>
                    {answerChoices.map((choice, index) => (
                        <div key={index}>
                            <input
                                type="radio"
                                id={choice}
                                name="answer"
                                value={choice}
                                checked={selectedAnswer === choice}
                                onChange={e => setSelectedAnswer(e.target.value)}
                            />
                            <label htmlFor={choice}>{choice}</label>
                        </div>
                    ))}
                    <button onClick={() => setCurrentQuestionIndex(currentQuestionIndex + 1)}>
                        Next
                    </button>
                </div>
            )}
        </div>
    );
}

export default PersonalityQuiz;
