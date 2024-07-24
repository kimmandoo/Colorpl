// src/components/Logout.js
import React from 'react';
import { useNavigate } from 'react-router-dom';
import { logout } from '../api/auth';

const Logout = () => {
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await logout();
      localStorage.removeItem('token');
      alert('Logged out successfully');
      navigate('/login');
    } catch (error) {
      console.error('Logout failed', error);
      alert('Logout failed');
    }
  };

  return (
    <button onClick={handleLogout}>Logout</button>
  );
};

export default Logout;
