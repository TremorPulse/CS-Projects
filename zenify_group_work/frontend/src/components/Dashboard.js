import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/style.css';

class Dashboard extends React.Component {
  render() {
    return (
       <div>
        <h1>Dashboard</h1>
        <ul>
            <li><Link to="/dashboard/personality-quiz">Personality Quiz</Link></li>
            <li><Link to="/dashboard/social-hub">Social Hub</Link></li>
            <li><Link to="/dashboard/weekly-quiz">Weekly Quiz</Link></li>
            <li><Link to="/StepData">Step Data</Link></li>
            <li><Link to="/Goals">Choose your goal</Link></li>
            <li><Link to="/MediaPlayer">Well being content</Link></li>
            <li><a href="default.asp">Home</a></li>
        </ul>
        <div className="container"> 
          <div className="circle">
            <span>We Are Zenify</span>
          </div>
          <div className="text-strip">
            <p>Crafting balance in work and life for everyone's well-being</p>
          </div>
        </div>
      </div>
    );
  }
}
export default Dashboard;


