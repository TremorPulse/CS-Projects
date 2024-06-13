import React, { useState, useContext } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../AuthContext';

const Register = () => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [company, setCompany] = useState('');  // Add a state variable for the company
    const [department, setDepartment] = useState('');  // Add a state variable for the department
    // const [verificationCode, setVerificationCode] = useState('');
    const [isVerificationStep, setIsVerificationStep] = useState(false);
    const [message, setMessage] = useState('');
    const [otp, setOtp] = useState(''); 
    const [uid, setUid] = useState('');
    const { setIsLoggedIn } = useContext(AuthContext);

    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();

        const data = {
            username: username,
            email: email,
            password: password,
            company: company,
            department: department
        };

        try {
            const response = await axios.post('register/', data, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            console.log(response.data);
            setIsVerificationStep(true);
            setMessage('Check your email');
            setUid(response.data.uid);
        } catch (error) {
            console.error('Error during registration', error);
            if (error.response) {
                setMessage(error.response.data.error || 'An error occurred during registration');
            } else if (error.request) {
                setMessage('No response received from the server. Please try again.');
            } else {
                setMessage('An error occurred. Please try again.');
            }
        }
    };

    const handleVerification = async (event) => {
        event.preventDefault();

        const data = {
            uid: uid,
            otp: otp
        };

        console.log('Sending data:', data);  // Log the data being sent

        try {
            const response = await axios.post('verify/', data, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            console.log('Server response:', response.data);  // Log the server response
            if (response.status === 200) {
                setIsLoggedIn(true);
                navigate('/dashboard');
            }
        } catch (error) {
            console.error('Error during verification', error);
            if (error.response) {
                console.log('Server error response:', error.response.data);  // Log the server error response
            }
        }
    };

    return isVerificationStep ? (
        <form onSubmit={handleVerification}>
            <div className="form-group">
                <label htmlFor="InputOtp">OTP</label>
                <input type="text" className="form-control" id="InputOtp" placeholder="Enter OTP" value={otp} onChange={e => setOtp(e.target.value)} required />
            </div>
            <button type="submit" className="btn btn-primary">Verify</button>
        </form>
    ) : (
        <form onSubmit={handleSubmit}>
            <div className="form-group">
                <label htmlFor="InputUsername">Username</label>
                <input type="text" className="form-control" id="InputUsername" placeholder="Enter username" value={username} onChange={e => setUsername(e.target.value)} required />
            </div>
            <div className="form-group">
                <label htmlFor="InputEmail">Email address</label>
                <input type="email" className="form-control" id="InputEmail" placeholder="Enter email" value={email} onChange={e => setEmail(e.target.value)} required />
            </div>
            <div className="form-group">
                <label htmlFor="InputCompany">Company</label>
                <input type="text" className="form-control" id="InputCompany" placeholder="Enter company" value={company} onChange={e => setCompany(e.target.value)} required />
            </div>
            <div className="form-group">
                <label htmlFor="InputDepartment">Department</label>
                <input type="text" className="form-control" id="InputDepartment" placeholder="Enter department" value={department} onChange={e => setDepartment(e.target.value)} required />
            </div>
            <div className="form-group">
                <label htmlFor="InputPassword">Password</label>
                <input type="password" className="form-control" id="InputPassword" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} required />
            </div>
            <button type="submit" className="btn btn-primary">Register</button>
            {message && <p>{message}</p>}
        </form>
    );
};

export default Register;
