// src/App.js
import React, { useState } from 'react';
import Signup from './components/Signup';
import Login from './components/Login';
import AdminProfile from './components/AdminProfile';
import AdminList from './components/AdminList';
import Sidebar from './components/Sidebar';
import Dashboard from './components/Dashboard';
import UserManagement from './components/UserManagement';
import ReviewManagement from './components/ReviewManagement';
import CommentManagement from './components/CommentManagement';
import api from './api/api';
import './styles/App.css';

function App() {
  const [token, setToken] = useState(null);
  const [isSuperAdmin, setIsSuperAdmin] = useState(false);
  const [activeComponent, setActiveComponent] = useState('dashboard');

  const handleLogin = (accessToken, role) => {
    setToken(accessToken);
    setIsSuperAdmin(role === 1);
  };

  const handleLogout = async () => {
    try {
      await api.post('/logout', {}, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setToken(null);
      setIsSuperAdmin(false);
    } catch (error) {
      alert(error.response.data.detail);
    }
  };

  const renderComponent = () => {
    switch (activeComponent) {
      case 'dashboard':
        return <Dashboard />;
      case 'userManagement':
        return <UserManagement />;
      case 'reviewManagement':
        return <ReviewManagement />;
      case 'commentManagement':
        return <CommentManagement />;
      default:
        return <Dashboard />;
    }
  };

  return (
    <div className="App">
      {!token ? (
        <>
          <Signup />
          <Login setToken={handleLogin} />
        </>
      ) : (
        <>
          <Sidebar
            activeComponent={activeComponent}
            setActiveComponent={setActiveComponent}
            handleLogout={handleLogout} // 로그아웃 핸들러 추가
          />
          <div className="content">
            {renderComponent()}
          </div>
        </>
      )}
    </div>
  );
}

export default App;
