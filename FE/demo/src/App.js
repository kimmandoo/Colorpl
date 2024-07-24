import React from 'react';
import { Route, Routes, Link } from 'react-router-dom';
import Signup from './components/Signup';
import Login from './components/Login';
import AdminList from './components/AdminList';
import Logout from './components/Logout';

const App = () => {
  return (
    <div>
      <nav>
        <ul>
          <li><Link to="/signup">Signup</Link></li>
          <li><Link to="/login">Login</Link></li>
          <li><Link to="/admin-list">Admin List</Link></li>
          <li><Logout /></li>
        </ul>
      </nav>
      <Routes>
        <Route path="/signup" element={<Signup />} />
        <Route path="/login" element={<Login />} />
        <Route path="/admin-list" element={<AdminList />} />
      </Routes>
    </div>
  );
};

export default App;
