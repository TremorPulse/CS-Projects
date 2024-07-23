import React, { useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../AuthContext';

const Logout = () => {
    const { isLoggedIn, setIsLoggedIn } = useContext(AuthContext);
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (!isLoggedIn && !loading) {
            navigate('/');
        }
    }, [isLoggedIn, loading]);

    const handleLogout = () => {
        setLoading(true);
        setIsLoggedIn(false);
        setTimeout(() => {
            setLoading(false);
            navigate('/');
        }, 1000);
    };

    if (loading) {
        return null;
    }

    return (
        <button onClick={handleLogout}>Logout</button>
    );
};

export default Logout;
