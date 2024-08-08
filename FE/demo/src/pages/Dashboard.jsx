// pages/Dashboard.jsx
import React, { useState } from 'react';
import { Routes, Route } from 'react-router-dom';
import { Box } from '@mui/material';
import Sidebar from '../components/Sidebar';
import TopRightIcons from '../components/TopRightIcons';
import Members from './Members';
import Reviews from './Reviews';
import Comments from './Comments';
import Tickets from './Tickets';
import DashboardHome from './DashboardHome';

const Dashboard = () => {
  const [sidebarExpanded, setSidebarExpanded] = useState(false);

  return (
    <Box display="flex">
      <Sidebar setSidebarExpanded={setSidebarExpanded} />
      <Box component="main" flexGrow={1}>
        <TopRightIcons />
        <Box p={3}>
          <Routes>
            <Route path="/" element={<DashboardHome />} />
            <Route path="members" element={<Members />} />
            <Route path="reviews" element={<Reviews />} />
            <Route path="comments" element={<Comments />} />
            <Route path="tickets" element={<Tickets />} />
          </Routes>
        </Box>
      </Box>
    </Box>
  );
};

export default Dashboard;
