import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import { CssBaseline, Box } from '@mui/material';
import Sidebar from './components/Sidebar';
import Header from './components/Header';
import Members from './pages/Members';
import Reviews from './pages/Reviews';
import Comments from './pages/Comments';
import Tickets from './pages/Tickets';
import LoginScreen from './pages/LoginScreen';
import PrivateRoute from './components/PrivateRoute';
import DashboardHome from './pages/DashboardHome';
import MemberModal from './components/MemberModal';
import ReviewModal from './components/ReviewModal';
import CommentModal from './components/CommentModal';
import TicketModal from './components/TicketModal';
import api from './api';

const App = () => {
  const [modalState, setModalState] = useState({ type: '', data: null });

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

  return (
    <Router>
      <CssBaseline />
      <Routes>
        <Route path="/login" element={<LoginScreen />} />
        <Route
          path="*"
          element={
            <PrivateRoute>
              <Box display="flex">
                <Sidebar />
                <Box component="main" flexGrow={1} ml={30}>
                  <Header />
                  <Box p={3} mt={8}>
                    <Routes>
                      <Route path="/" element={<Navigate replace to="/dashboard" />} />
                      <Route path="/dashboard" element={<DashboardHome />} />
                      <Route path="/members" element={<Members onViewMember={handleViewMember} />} />
                      <Route path="/reviews" element={<Reviews onViewReview={handleViewReview} />} />
                      <Route path="/comments" element={<Comments onViewComment={handleViewComment} />} />
                      <Route path="/tickets" element={<Tickets onViewTicket={handleViewTicket} />} />
                    </Routes>
                  </Box>
                </Box>
              </Box>
            </PrivateRoute>
          }
        />
      </Routes>
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
        onViewReview={handleViewReview}
      />
    </Router>
  );
};

export default App;
