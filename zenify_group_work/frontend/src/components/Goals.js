import React, { useEffect, useState } from 'react';
import { useStepGoal } from './StepGoalContext';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const Goals = () => {
    const [goals, setGoals] = useState([]);
    const [selectedGoal, setSelectedGoal] = useState('');
    const { setStepGoal } = useStepGoal();
    const navigate = useNavigate();

    useEffect(() => {
        // Fetch goals when the component mounts
        axios.get('http://127.0.0.1:8000/goals/')
            .then(response => {
                setGoals(response.data);
                if (response.data.length > 0) {
                    setSelectedGoal(response.data[0].id); // Default to the first goal
                }
            })
            .catch(error => console.error('Error fetching goals:', error));
    }, []);

    const handleSubmit = (event) => {
        event.preventDefault();
        // Find the selected goal and update the context
        const goal = goals.find(g => g.id === parseInt(selectedGoal, 10));
        if (goal) {
            setStepGoal(goal.steps); 
            navigate('/stepdata'); 
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <label htmlFor="goal-select">Choose a goal:</label>
            <select
                id="goal-select"
                value={selectedGoal}
                onChange={e => setSelectedGoal(e.target.value)}
            >
                {goals.map(goal => (
                    <option key={goal.id} value={goal.id}>
                        {goal.description} - {goal.steps} steps
                    </option>
                ))}
            </select>
            <button type="submit">Submit Goal</button>
        </form>
    );
};

export default Goals;
