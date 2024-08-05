// components/Header.jsx
import React from 'react';
import { AppBar, Toolbar, IconButton, Box } from '@mui/material';
import AccountCircle from '@mui/icons-material/AccountCircle';
import NotificationsIcon from '@mui/icons-material/Notifications';
import LogoutIcon from '@mui/icons-material/Logout';
import { useNavigate } from 'react-router-dom';
import api from '../api';

const Header = () => {
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await api.post('/auth/logout');
      localStorage.removeItem('token');
      navigate('/login');
    } catch (error) {
      console.error('Logout failed', error);
    }
  };

  return (
    <AppBar position="fixed" sx={{ zIndex: 1300, backgroundColor: 'inherit', boxShadow: 'none' }}>
      <Toolbar>
        <Box sx={{ flexGrow: 1 }} />
        <IconButton color="inherit" onClick={() => navigate('/dashboard')}>
          <NotificationsIcon sx={{ color: 'black' }} />
        </IconButton>
        <IconButton color="inherit" onClick={() => navigate('/dashboard')}>
          <AccountCircle sx={{ color: 'black' }} />
        </IconButton>
        <IconButton color="inherit" onClick={handleLogout}>
          <LogoutIcon sx={{ color: 'black' }} />
        </IconButton>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
