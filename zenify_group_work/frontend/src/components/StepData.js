import React, { useEffect, useState, useContext } from 'react';
import axios from 'axios';
import { useStepGoal } from './StepGoalContext';
import DonutChart from './DonutChart';
import { AuthContext } from '../AuthContext';

const StepData = () => {
    const [steps, setSteps] = useState(null);
    const [error, setError] = useState('');
    const { stepGoal } = useStepGoal(); // Use context
    const { isLoggedIn, setLoggedIn } = useContext(AuthContext);

    useEffect(() => {
        axios.get('http://127.0.0.1:8000/get-steps/', { withCredentials: true })
            .then(response => {
                setSteps(response.data.steps);
            })
            .catch(error => {
                console.error('Error fetching step data:', error);
                setError('Failed to fetch steps');
            });
    }, []);

    if (error) {
        return <p>Error: {error}</p>;
    }

    return (
        <div>
            <h1>Steps Data</h1>
            <p>Steps: {steps}</p>
            <div className="chart-container">
                <DonutChart steps={steps} stepGoal={stepGoal} />
            </div>
        </div>
    );
};

export default StepData;
