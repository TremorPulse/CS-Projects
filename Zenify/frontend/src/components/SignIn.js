// SignIn.js
import React from 'react';

const SignIn = () => {
    const signIn = () => {
        window.location.href = 'http://127.0.0.1:8000/google/login/';
    };

    return <button onClick={signIn}>Sign In with Google</button>;
};

export default SignIn;
