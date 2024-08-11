import React, { useState, useEffect } from 'react';
import { Routes, Route, useLocation } from 'react-router-dom';
import { Box } from '@mui/material';
import Sidebar from '../components/Sidebar';
import TopRightIcons from '../components/TopRightIcons';
import Members from './Members';
import Reviews from './Reviews';
import Comments from './Comments';
import Tickets from './Schedules';
import DashboardHome from './DashboardHome';

const Dashboard = () => {
  const [sidebarExpanded, setSidebarExpanded] = useState(false);
  const location = useLocation();

  useEffect(() => {
    // 로그인 후 대시보드로 이동할 때 사이드바를 확장된 상태로 설정
    setSidebarExpanded(true);
  }, [location]);

  return (
    <Box display="flex">
      <Sidebar setSidebarExpanded={setSidebarExpanded} expanded={sidebarExpanded} />
      <Box component="main" flexGrow={1}>
        <TopRightIcons />
        <Box p={3}>
          <Routes>
            <Route path="/" element={<DashboardHome />} />
            <Route path="members" element={<Members />} />
            <Route path="Schedules" element={<Schedules />} />
            <Route path="reviews" element={<Reviews />} />
            <Route path="comments" element={<Comments />} />
          </Routes>
        </Box>
      </Box>
    </Box>
  );
};

export default Dashboard;
