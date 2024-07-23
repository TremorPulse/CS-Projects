import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';

const VerifyEmail = () => {
    const { uidb64, token } = useParams();
    const [verificationCode, setVerificationCode] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        setVerificationCode(token);
    }, [token]);

    const handleSubmit = async (event) => {
        event.preventDefault();

        navigate('/');
    };

    return (
        <form onSubmit={handleSubmit}>
            <div className="form-group">
                <label htmlFor="InputVerificationCode">Verification Code</label>
                <input type="text" className="form-control" id="InputVerificationCode" placeholder="Enter verification code" value={verificationCode} readOnly />
            </div>
            <button type="submit" className="btn btn-primary">Confirm Verification Code</button>
        </form>
    );
};

export default VerifyEmail;
