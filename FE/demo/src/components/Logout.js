import React from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const Logout = () => {
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      const token = localStorage.getItem('token');
      if (token) {
        await axios.post('http://localhost:8000/logout', {}, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
      }
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
