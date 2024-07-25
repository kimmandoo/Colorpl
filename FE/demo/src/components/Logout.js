// src/components/Logout.js
import React from 'react';
import { useNavigate } from 'react-router-dom';
import { logout } from '../api/auth'; // 로그아웃 API 호출 함수

const Logout = () => {
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      const token = localStorage.getItem('token');
      if (token) {
        await logout(token);
        localStorage.removeItem('token');
        alert('Logged out successfully');
        navigate('/login');
      } else {
        alert('No token found');
      }
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
