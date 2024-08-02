import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import InitialScreen from './components/InitialScreen';
import LoginScreen from './components/auth/LoginScreen';
import Dashboard from './components/dashboard/Dashboard';

const PrivateRoute = ({ children }) => {
  const token = localStorage.getItem('token');
  return token ? children : <Navigate to="/login" />;
};

function App() {
  return (
    //<Router>
    <Routes>
      <Route path="/" element={<InitialScreen />} />
      <Route path="/login" element={<LoginScreen />} />
      <Route path="/dashboard/*" element={<PrivateRoute><Dashboard /></PrivateRoute>} />
    </Routes>
    //</Router>
  );
}

export default App;
