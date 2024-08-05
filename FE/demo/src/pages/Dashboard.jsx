import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { Box } from '@mui/material';
import Sidebar from '../components/Sidebar';
import Header from '../components/Header';
import Members from './Members';
import Reviews from './Reviews';
import Comments from './Comments';
import DashboardHome from './DashboardHome';

const Dashboard = () => {
  return (
    <Box display="flex">
      <Sidebar />
      <Box component="main" flexGrow={1}>
        <Header />
        <Box p={3}>
          <Routes>
            <Route path="/" element={<DashboardHome />} />
            <Route path="members" element={<Members />} />
            <Route path="reviews" element={<Reviews />} />
            <Route path="comments" element={<Comments />} />
          </Routes>
        </Box>
      </Box>
    </Box>
  );
};

export default Dashboard;
