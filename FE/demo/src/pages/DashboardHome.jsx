// pages/DashboardHome.jsx
import React from 'react';
import { Box, Paper } from '@mui/material';

const DashboardHome = () => {
  return (
    <Box display="flex" flexWrap="wrap" justifyContent="center" p={2} gap={2} alignItems="center" height="100vh">
      <Paper elevation={3} sx={{ width: 'calc(50% - 16px)', height: '300px' }} />
      <Paper elevation={3} sx={{ width: 'calc(50% - 16px)', height: '300px' }} />
      <Paper elevation={3} sx={{ width: 'calc(50% - 16px)', height: '300px' }} />
      <Paper elevation={3} sx={{ width: 'calc(50% - 16px)', height: '300px' }} />
      <Paper elevation={3} sx={{ width: '100%', height: '400px' }} />
    </Box>
  );
};

export default DashboardHome;
