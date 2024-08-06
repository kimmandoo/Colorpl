import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import { CssBaseline, Box, ThemeProvider } from '@mui/material';
import Sidebar from './components/Sidebar';
import TopRightIcons from './components/TopRightIcons';
import Members from './pages/Members';
import Reviews from './pages/Reviews';
import Comments from './pages/Comments';
import Tickets from './pages/Tickets';
import LoginScreen from './pages/LoginScreen';
import PrivateRoute from './components/PrivateRoute';
import DashboardHome from './pages/DashboardHome';
import AdminManagement from './pages/AdminManagement';
import MemberModal from './components/MemberModal';
import ReviewModal from './components/ReviewModal';
import CommentModal from './components/CommentModal';
import TicketModal from './components/TicketModal';
import api from './api';
import theme from './theme';

const App = () => {
  const [modalState, setModalState] = useState({ type: '', data: null });
  const [user, setUser] = useState(null);
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [expandedMenu, setExpandedMenu] = useState('');

  const handleCloseModal = () => setModalState({ type: '', data: null });

  const handleViewMember = async (member_id) => {
    try {
      const response = await api.get(`/cs2/members/${member_id}/activity`);
      setModalState({ type: 'member', data: response.data });
    } catch (error) {
      console.error(error);
    }
  };

  const handleViewReview = async (review_id) => {
    try {
      const response = await api.get(`/cs2/reviews/${review_id}/activity`);
      setModalState({ type: 'review', data: response.data });
    } catch (error) {
      console.error(error);
    }
  };

  const handleViewComment = async (comment_id) => {
    try {
      const response = await api.get(`/cs2/comments/${comment_id}/activity`);
      setModalState({ type: 'comment', data: response.data });
    } catch (error) {
      console.error(error);
    }
  };

  const handleViewTicket = async (ticket_id) => {
    try {
      const response = await api.get(`/cs2/tickets/${ticket_id}/activity`);
      setModalState({ type: 'ticket', data: response.data });
    } catch (error) {
      console.error(error);
    }
  };

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
              onProfileClick={() => setModalState({ type: 'profile', data: user })}
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
            <Route path="/members" element={<PrivateRoute><Members onViewMember={handleViewMember} /></PrivateRoute>} />
            <Route path="/reviews" element={<PrivateRoute><Reviews onViewReview={handleViewReview} /></PrivateRoute>} />
            <Route path="/comments" element={<PrivateRoute><Comments onViewComment={handleViewComment} /></PrivateRoute>} />
            <Route path="/tickets" element={<PrivateRoute><Tickets onViewTicket={handleViewTicket} /></PrivateRoute>} />
            {user && user.role === 1 && (
              <Route path="/admin-management" element={<PrivateRoute><AdminManagement /></PrivateRoute>} />
            )}
          </Routes>
        </Box>
        <MemberModal
          open={modalState.type === 'member'}
          onClose={handleCloseModal}
          member={modalState.data}
          onViewTicket={handleViewTicket}
          onViewReview={handleViewReview}
          onViewComment={handleViewComment}
        />
        <ReviewModal
          open={modalState.type === 'review'}
          onClose={handleCloseModal}
          review={modalState.data}
          onViewMember={handleViewMember}
          onViewTicket={handleViewTicket}
          onViewComment={handleViewComment}
        />
        <CommentModal
          open={modalState.type === 'comment'}
          onClose={handleCloseModal}
          comment={modalState.data}
          onViewMember={handleViewMember}
          onViewReview={handleViewReview}
        />
        <TicketModal
          open={modalState.type === 'ticket'}
          onClose={handleCloseModal}
          ticket={modalState.data}
          onViewMember={handleViewMember}
        />
      </Router>
    </ThemeProvider>
  );
};

export default App;
