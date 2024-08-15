import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import { CssBaseline, Box, ThemeProvider } from '@mui/material';
import Sidebar from './components/Sidebar';
import TopRightIcons from './components/TopRightIcons';
import Members from './pages/Members';
import MemberDetail from './pages/MemberDetail';
import Reviews from './pages/Reviews';
import ReviewDetail from './pages/ReviewDetail';
import Comments from './pages/Comments';
import CommentDetail from './pages/CommentDetail';
import Schedules from './pages/Schedules';
import ScheduleDetail from './pages/ScheduleDetail';
import ScheduleImageUpdate from './pages/ScheduleImageUpdate';
import Reservations from './pages/Reservations';
import ReservationDetail from './pages/ReservationDetail';
import LoginScreen from './pages/LoginScreen';
import PrivateRoute from './components/PrivateRoute';
import DashboardHome from './pages/DashboardHome';
import AdminManagement from './pages/AdminManagement';
import AdminDetail from './pages/AdminDetail';
import MultiStepForm from './pages/MultiStepForm';
import TheatersTablePage from './pages/TheatersTablePage';
import TheaterHallPage from './pages/TheaterHallPage';
import api from './api';
import theme from './theme';

const App = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true); // 로딩 상태 추가
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [expandedMenu, setExpandedMenu] = useState('');

  const handleLogout = async () => {
    try {
      await api.post('/auth/logout');
      setUser(null); // 로그아웃 시 사용자 상태를 명확히 null로 설정
      localStorage.removeItem('token');
      window.location.href = '/login'; // 로그인 페이지로 강제 리다이렉트
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
      setLoading(false); // 로딩 완료
    };
    fetchUser();
  }, []);

  if (loading) {
    return <div>Loading...</div>; // 로딩 상태에서 사이드바 렌더링 방지
  }

  return (
    <ThemeProvider theme={theme}>
      <Router>
        <CssBaseline />
        {user === null ? (
          <div></div> // 로딩 중 메시지 또는 스피너 표시
        ) : (
          <>
            <Sidebar
              user={user}
              sidebarOpen={sidebarOpen}
              setSidebarOpen={setSidebarOpen}
              expandedMenu={expandedMenu}
              setExpandedMenu={setExpandedMenu}
            />
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
            <Route path="/dashboard" element={<PrivateRoute><DashboardHome user={user} /></PrivateRoute>} />
            <Route path="/members" element={<PrivateRoute><Members user={user} /></PrivateRoute>} />
            <Route path="/members/:member_id" element={<PrivateRoute><MemberDetail user={user} /></PrivateRoute>} />
            <Route path="/reviews" element={<PrivateRoute><Reviews user={user} /></PrivateRoute>} />
            <Route path="/reviews/:review_id" element={<PrivateRoute><ReviewDetail user={user} /></PrivateRoute>} />
            <Route path="/comments" element={<PrivateRoute><Comments user={user} /></PrivateRoute>} />
            <Route path="/comments/:comment_id" element={<PrivateRoute><CommentDetail user={user} /></PrivateRoute>} />
            <Route path="/schedules" element={<PrivateRoute><Schedules user={user} /></PrivateRoute>} /> 
            <Route path="/schedules/:schedule_id" element={<PrivateRoute><ScheduleDetail user={user} /></PrivateRoute>} /> 
            <Route path="/schedules/:schedule_id/image" element={<PrivateRoute><ScheduleImageUpdate user={user} /></PrivateRoute>} /> 
            <Route path="/reservations" element={<PrivateRoute><Reservations user={user} /></PrivateRoute>} />
            <Route path="/reservations/:reservation_id" element={<PrivateRoute><ReservationDetail user={user} /></PrivateRoute>} />
            <Route path="/theaters" element={<PrivateRoute><TheatersTablePage user={user} /></PrivateRoute>} />
            <Route path="/theaters/:theater_id" element={<PrivateRoute><TheaterHallPage user={user} /></PrivateRoute>} />
            <Route path="/register-show" element={<PrivateRoute><MultiStepForm user={user} /></PrivateRoute>} />
            <Route path="/theaters" element={<PrivateRoute><TheatersTablePage user={user} /></PrivateRoute>} />
            {user && user.role === 1 && (
              <Route path="/admin-management" element={<PrivateRoute><AdminManagement user={user} /></PrivateRoute>} />
            )}
            <Route path="/admin-detail/:adminId" element={<PrivateRoute><AdminDetail user={user} /></PrivateRoute>} />
          </Routes>
        </Box>
      </Router>
    </ThemeProvider>
  );
};

export default App;
