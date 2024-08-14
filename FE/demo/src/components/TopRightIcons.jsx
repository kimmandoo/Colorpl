import React, { useState } from 'react';
import { Box, IconButton, Menu, MenuItem, Avatar, Typography, Divider } from '@mui/material';
import AccountCircle from '@mui/icons-material/AccountCircle';
import NotificationsIcon from '@mui/icons-material/Notifications';
import SettingsIcon from '@mui/icons-material/Settings';
import LogoutIcon from '@mui/icons-material/Logout';
import { useNavigate } from 'react-router-dom';

const TopRightIcons = ({ user, onProfileClick, onNotificationsClick, onSettingsClick, onLogout }) => {
  const [anchorEl, setAnchorEl] = useState(null);
  const navigate = useNavigate();

  const handleMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleAdminManagement = () => {
    handleMenuClose();
    navigate('/admin-management');
  };

  return (
    <Box sx={{ position: 'fixed', top: 8, right: 8, display: 'flex', gap: 1 }}>
      <IconButton color="inherit" onClick={onNotificationsClick}>
        <NotificationsIcon sx={{ color: 'black' }} />
      </IconButton>
      <IconButton color="inherit" onClick={onSettingsClick}>
        <SettingsIcon sx={{ color: 'black' }} />
      </IconButton>
      <IconButton color="inherit" onClick={handleMenuOpen}>
        {user?.profile?.image_url ? (
          <Avatar src={user.profile.image_url} alt="Profile Image" sx={{ width: 40, height: 40 }} />
        ) : (
          <AccountCircle sx={{ color: 'black' }} />
        )}
      </IconButton>
      <IconButton color="inherit" onClick={onLogout}>
        <LogoutIcon sx={{ color: 'black' }} />
      </IconButton>
      <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={handleMenuClose}>
        <Box sx={{ p: 2 }}>
          <Typography variant="subtitle1">{user?.username}</Typography>
          <Typography variant="body2">{user?.email}</Typography>
        </Box>
        <Divider />
        <MenuItem onClick={onProfileClick}>프로필 수정</MenuItem>
        {user?.role === 1 && <MenuItem onClick={handleAdminManagement}>관리자 관리</MenuItem>}
      </Menu>
    </Box>
  );
};

export default TopRightIcons;
