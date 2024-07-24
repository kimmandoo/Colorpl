// src/components/Sidebar.js
import React, { useState } from 'react';
import { FaBars, FaSignOutAlt } from 'react-icons/fa';
import '../styles/Sidebar.css';

const Sidebar = ({ activeComponent, setActiveComponent, handleLogout }) => {
  const [isCollapsed, setIsCollapsed] = useState(false);

  const handleToggle = () => {
    setIsCollapsed(!isCollapsed);
  };

  return (
    <div className={`sidebar ${isCollapsed ? 'collapsed' : ''}`}>
      <div className="sidebar-toggle" onClick={handleToggle}>
        <FaBars />
      </div>
      <div
        className={`sidebar-item ${activeComponent === 'dashboard' ? 'active' : ''}`}
        onClick={() => setActiveComponent('dashboard')}
      >
        <span className="sidebar-item-text">Dashboard</span>
      </div>
      <div
        className={`sidebar-item ${activeComponent === 'userManagement' ? 'active' : ''}`}
        onClick={() => setActiveComponent('userManagement')}
      >
        <span className="sidebar-item-text">User Management</span>
      </div>
      <div
        className={`sidebar-item ${activeComponent === 'reviewManagement' ? 'active' : ''}`}
        onClick={() => setActiveComponent('reviewManagement')}
      >
        <span className="sidebar-item-text">Review Management</span>
      </div>
      <div
        className={`sidebar-item ${activeComponent === 'commentManagement' ? 'active' : ''}`}
        onClick={() => setActiveComponent('commentManagement')}
      >
        <span className="sidebar-item-text">Comment Management</span>
      </div>
      <div className="sidebar-item" onClick={handleLogout}>
        <FaSignOutAlt />
        <span className="sidebar-item-text">Logout</span>
      </div>
    </div>
  );
};

export default Sidebar;
