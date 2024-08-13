import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import { CssBaseline, Box, ThemeProvider } from '@mui/material';
import Sidebar from './components/Sidebar';
import TopRightIcons from './components/TopRightIcons';
import Members from './pages/Members';
import MemberDetail from './pages/MemberDetail'; // 멤버 상세 페이지
import Reviews from './pages/Reviews';
import ReviewDetail from './pages/ReviewDetail';
import Comments from './pages/Comments';
import CommentDetail from './pages/CommentDetail';
import Schedules from './pages/Schedules'; // Schedules 페이지 추가
import ScheduleDetail from './pages/ScheduleDetail'; // Schedule 상세 페이지
import ScheduleImageUpdate from './pages/ScheduleImageUpdate'; // Schedule 이미지 업데이트 페이지
import LoginScreen from './pages/LoginScreen';
import PrivateRoute from './components/PrivateRoute';
import DashboardHome from './pages/DashboardHome';
import AdminManagement from './pages/AdminManagement';
import api from './api';
import theme from './theme';

const App = () => {
  const [user, setUser] = useState(null);
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [expandedMenu, setExpandedMenu] = useState('');

  const handleLogout = async () => {
    try {
      await api.post('/auth/logout');
      localStorage.removeItem('token');
      window.location.href = '/login';
    } catch (error) {
      console.error('Logout failed', error);
    }
  };

  useEffect(() => {
    const fetchUser = async () => {
      const token = localStorage.getItem('token');
      if (token) {
        try {
          const response = await api.get('/auth/administrators/me', {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          setUser(response.data);
        } catch (error) {
          console.error('Failed to fetch user', error);
        }
      }
    };
    fetchUser();
  }, []);

  return (
    <ThemeProvider theme={theme}>
      <Router>
        <CssBaseline />
        {user && (
          <>
            <Sidebar sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen} expandedMenu={expandedMenu} setExpandedMenu={setExpandedMenu} />
            <TopRightIcons
              user={user}
              onProfileClick={() => console.log("Profile clicked")}
              onNotificationsClick={() => {}}
              onSettingsClick={() => {}}
              onLogout={handleLogout}
            />
          </>
        )}
        <Box
          component="main"
          sx={{
            flexGrow: 1,
            mt: 4,
            ml: sidebarOpen ? (expandedMenu ? '280px' : '80px') : '80px',
            width: `calc(100% - ${sidebarOpen ? (expandedMenu ? '480px' : '80px') : '80px'})`,
            transition: 'margin 0.3s, width 0.3s',
          }}
        >
          <Routes>
            <Route path="/login" element={<LoginScreen />} />
            <Route path="/" element={user ? <Navigate replace to="/dashboard" /> : <Navigate replace to="/login" />} />
            <Route path="/dashboard" element={<PrivateRoute><DashboardHome /></PrivateRoute>} />
            <Route path="/members" element={<PrivateRoute><Members /></PrivateRoute>} />
            <Route path="/members/:member_id" element={<PrivateRoute><MemberDetail /></PrivateRoute>} />
            <Route path="/reviews" element={<PrivateRoute><Reviews /></PrivateRoute>} />
            <Route path="/reviews/:review_id" element={<PrivateRoute><ReviewDetail /></PrivateRoute>} />
            <Route path="/comments" element={<PrivateRoute><Comments /></PrivateRoute>} />
            <Route path="/comments/:comment_id" element={<PrivateRoute><CommentDetail /></PrivateRoute>} />
            <Route path="/schedules" element={<PrivateRoute><Schedules /></PrivateRoute>} /> {/* Schedules 경로 추가 */}
            <Route path="/schedules/:schedule_id" element={<PrivateRoute><ScheduleDetail /></PrivateRoute>} /> ScheduleDetail 경로 추가
            <Route path="/schedules/:schedule_id/image" element={<PrivateRoute><ScheduleImageUpdate /></PrivateRoute>} /> {/* ScheduleImageUpdate 경로 추가 */}
            {user && user.role === 1 && (
              <Route path="/admin-management" element={<PrivateRoute><AdminManagement /></PrivateRoute>} />
            )}
          </Routes>
        </Box>
      </Router>
    </ThemeProvider>
  );
};

export default App;
