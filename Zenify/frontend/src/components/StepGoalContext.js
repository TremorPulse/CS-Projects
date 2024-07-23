// src/contexts/StepGoalContext.js
import React, { createContext, useState, useContext } from 'react';

const StepGoalContext = createContext();

export const useStepGoal = () => useContext(StepGoalContext);

export const StepGoalProvider = ({ children }) => {
    const [stepGoal, setStepGoal] = useState(10000); 

    return (
        <StepGoalContext.Provider value={{ stepGoal, setStepGoal }}>
            {children}
        </StepGoalContext.Provider>
    );
};
