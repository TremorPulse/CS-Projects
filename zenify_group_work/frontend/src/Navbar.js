import React, { useContext } from 'react';
import { AuthContext } from './AuthContext';
import { Link } from 'react-router-dom';
import './styles/navbar.css';
import logo from './static/logo2.png';
import facebookIcon from './static/facebook.png';
import linkedinIcon from './static/linkedin.png';
import twitterIcon from './static/twitter.png';

const Navbar = () => {
  const { isLoggedIn, setLoggedIn } = useContext(AuthContext);

  const handleLogout = () => {
    setLoggedIn(false);
  };

    return (
        <nav className="navbar">
            <div className="nav-container">
                <Link to="/"><img src={logo} alt="Logo" className="nav-logo"/></Link>
                <ul className="nav-list">
                    <li className="nav-item"><Link to="/" className="nav-link"> Home </Link></li>
                    <li className="nav-item"><Link to="/about" className="nav-link"> About Us </Link></li>
                    <li className="nav-item"><Link to="/contact" className="nav-link"> Contact </Link></li>
                    {isLoggedIn ? (
                        <>
                            <li className="nav-item"><Link to="/dashboard" className="nav-link"> Dashboard </Link></li>
                            <li className="nav-item"><Link to="/" className="nav-link" onClick={handleLogout}> Logout </Link></li>
                        </>
                    ) : (
                        <>
                            <li className="nav-item"><Link to="/login" className="nav-link"> Login </Link></li>
                            <li className="nav-item"><Link to="/register" className="nav-link"> Register </Link></li>
                        </>
                    )}
                </ul>
            </div>
            <div className="social-media-icons">
                <a href="https://www.facebook.com" target="_blank" rel="noopener noreferrer">
                    <img src={facebookIcon} alt="Facebook" />
                </a>
                <a href="https://www.x.com" target="_blank" rel="noopener noreferrer">
                    <img src={twitterIcon} alt="Twitter" />
                </a>
                <a href="https://www.linkedin.com" target="_blank" rel="noopener noreferrer">
                    <img src={linkedinIcon} alt="LinkedIn" />
                </a>
            </div>
        </nav>
    );
};

export default Navbar;
