// App.jsx
import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import { CssBaseline, Box } from '@mui/material';
import Sidebar from './components/Sidebar';
import Header from './components/Header';
import Members from './pages/Members';
import Reviews from './pages/Reviews';
import Comments from './pages/Comments';
import LoginScreen from './pages/LoginScreen';
import PrivateRoute from './components/PrivateRoute';
import DashboardHome from './pages/DashboardHome';

const App = () => {
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
                      <Route path="/members" element={<Members />} />
                      <Route path="/reviews" element={<Reviews />} />
                      <Route path="/comments" element={<Comments />} />
                    </Routes>
                  </Box>
                </Box>
              </Box>
            </PrivateRoute>
          }
        />
      </Routes>
    </Router>
  );
};

export default App;
