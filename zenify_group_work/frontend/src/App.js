// App.js
import axios from 'axios';
import React, { useState, useEffect } from 'react';
import './styles/App.css';
import Navbar from './Navbar';
import { AuthProvider } from './AuthContext';
import PersonalityQuiz from './components/PersonalityQuiz';
import Dashboard from './components/Dashboard';
import Login from './components/Login';
import Logout from './components/Logout';
import Register from './components/Register';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import successImage from './static/success.png';
import StepData from './components/StepData';
import SignIn from './components/SignIn';
import Goals from './components/Goals';
import MediaPlayer from './components/AudioVideoPlayer';
import { StepGoalProvider } from './components/StepGoalContext';
import 'video.js/dist/video-js.css';
import { Link } from 'react-router-dom';


const App = () => {
    const [isSignedIn, setIsSignedIn] = useState(false);

    useEffect(() => {
        // Check sign-in status with the backend - this is for google not the main AUTH 
        axios.get('http://127.0.0.1:8000/check-signin/', { withCredentials: true })
            .then(response => {
                setIsSignedIn(response.data.isSignedIn);
            })
            .catch(error => {
                console.error('Error checking sign-in status:', error);
            });
    }, []);

  return (
    <AuthProvider>
    <StepGoalProvider>
      <Router>
        <div className="App">
          <Navbar />
          <Routes>
              <>
                <Route path="/dashboard" element={<Dashboard />} />
                <Route path="/dashboard/personality-quiz" element={<PersonalityQuiz />} />
                <Route path="/login" element={<Login />} />
                <Route path="/logout" element={<Logout />} />
                <Route path="/register" element={<Register />} />
                <Route path="/StepData" element={<StepData />} />
                <Route path="/Goals" element={<Goals />} />
                <Route path="/MediaPlayer" element={<MediaPlayer />} />
                <Route path="/" element={
                  <>
                    <div className="App-header">
                      <div className="header-circle">We Are Zenify</div>
                      <div className="purple-bar">Crafting balance in work and life for everyone's well-being</div>
                    </div>
                    <main>
                      <section className="zenify-balance">
                        <div className="text-container">
                          <div className="zenify-title-container">
                            <p className="zenify-title">ZENIFY BALANCE</p>
                          </div>
                          <h2>Elevate Your Team, Enrich Your Work Life</h2>
                          <p>Embrace the path to a thriving workforce with tools and insights for optimal work-life synergy.</p>
                          <button className="btn primary">Get Started</button>
                          <button className="btn underline-btn">Learn More</button>
                        </div>
                        <img src={successImage} alt="Success" />
                      </section>

                      <section className="about-zenify">
                        <p>Cultivate a Balanced Work Environment!</p>
                        <button className="btn secondary">Learn More</button>
                      </section>

                      <section className="wellbeing-progress">
                        <h2>View Your Wellbeing Progress</h2>
                        <button className="btn primary" ><Link to="/Dashboard">Dashboard</Link></button>
                      </section>

                      <section className="advice-information">
                        <h2>Building a Healthier Workforce, Together</h2>
                      </section>
                    </main>
                  </>
                } />
                </>
            <Route path="/StepData" element={isSignedIn ? <StepData /> : <SignIn />} />
            <Route path="google/login" element={<SignIn />} />
              </Routes>
            <footer className="App-footer">
            </footer>
        </div>
      </Router>
    </StepGoalProvider>
    </AuthProvider>
  );
};

export default App;
