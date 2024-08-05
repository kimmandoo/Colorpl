// components/MainSidebar.jsx
import React from 'react';
import { Drawer, Box, Button } from '@mui/material';
import DashboardIcon from '@mui/icons-material/Dashboard';
import HomeIcon from '@mui/icons-material/Home';
import { useNavigate } from 'react-router-dom';

const MainSidebar = ({ setSidebarExpanded }) => {
  const navigate = useNavigate();

  const handleCsClick = () => {
    setSidebarExpanded(true);
  };

  const handleDashboard = () => {
    navigate('/dashboard');
  };

  return (
    <Drawer
      variant="permanent"
      sx={{ width: 60, flexShrink: 0, zIndex: 1200 }}
      PaperProps={{ style: { width: 60 } }}
    >
      <Box display="flex" flexDirection="column" alignItems="center" justifyContent="space-between" height="100vh" py={2}>
        <Box>
          <Button onClick={handleCsClick} sx={{ textTransform: 'none', padding: 0, minWidth: 48, height: 48 }}>
            <Box display="flex" flexDirection="column" alignItems="center">
              <DashboardIcon sx={{ color: 'black' }} />
              <Box component="span" sx={{ color: 'black', fontSize: '12px', mt: '2px' }}>CS</Box>
            </Box>
          </Button>
        </Box>
        <Box>
          <Button onClick={handleDashboard} sx={{ textTransform: 'none', padding: 0, minWidth: 48, height: 48 }}>
            <Box display="flex" flexDirection="column" alignItems="center">
              <HomeIcon sx={{ color: 'black' }} />
              <Box component="span" sx={{ color: 'black', fontSize: '12px', mt: '2px' }}>Home</Box>
            </Box>
          </Button>
        </Box>
      </Box>
    </Drawer>
  );
};

export default MainSidebar;
