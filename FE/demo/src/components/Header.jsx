// components/Header.jsx
import React from 'react';
import { AppBar, Toolbar, Box, IconButton } from '@mui/material';
import AccountCircle from '@mui/icons-material/AccountCircle';
import NotificationsIcon from '@mui/icons-material/Notifications';
import SettingsIcon from '@mui/icons-material/Settings';
import LogoutIcon from '@mui/icons-material/Logout';
import { useNavigate } from 'react-router-dom';
import api from '../api';

const Header = ({ onProfileClick, onNotificationsClick, onSettingsClick, onLogout }) => {
  const navigate = useNavigate();

  return (
    <AppBar position="fixed" sx={{ zIndex: 1300, backgroundColor: 'inherit', boxShadow: 'none' }}>
      <Toolbar>
        <Box sx={{ flexGrow: 1 }} />
        <Box sx={{ position: 'absolute', top: 8, right: 8 }}>
          <IconButton color="inherit" onClick={onNotificationsClick}>
            <NotificationsIcon sx={{ color: 'black' }} />
          </IconButton>
          <IconButton color="inherit" onClick={onSettingsClick}>
            <SettingsIcon sx={{ color: 'black' }} />
          </IconButton>
          <IconButton color="inherit" onClick={onProfileClick}>
            <AccountCircle sx={{ color: 'black' }} />
          </IconButton>
          <IconButton color="inherit" onClick={onLogout}>
            <LogoutIcon sx={{ color: 'black' }} />
          </IconButton>
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
