// pages/Dashboard.jsx
import React, { useState } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { Box } from '@mui/material';
import Sidebar from '../components/Sidebar';
import SubSidebar from '../components/SubSidebar';
import Header from '../components/Header';
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
      <SubSidebar sidebarExpanded={sidebarExpanded} setSidebarExpanded={setSidebarExpanded} />
      <Box component="main" flexGrow={1}>
        <Header />
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
