import React from 'react';
import { Box, List, ListItem, ListItemIcon, ListItemText, Typography } from '@mui/material';
import { People as PeopleIcon, Comment as CommentIcon, Movie as MovieIcon, Dashboard as DashboardIcon, Assignment as AssignmentIcon } from '@mui/icons-material';
import { Link } from 'react-router-dom';

const Sidebar = ({ user, sidebarOpen, setSidebarOpen, expandedMenu, setExpandedMenu }) => {

  const handleMouseEnter = (menu) => {
    setExpandedMenu(menu);
  };

  const handleMouseLeave = () => {
    setExpandedMenu('');
  };

  const handleItemClick = () => {
    setExpandedMenu('');
  };

  return (
    <Box sx={{ display: 'flex', height: '100%' }}>
      <Box
        sx={{
          width: 80,
          height: '100%',
          bgcolor: 'background.paper',
          position: 'fixed',
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          py: 2,
          overflowX: 'hidden',
        }}
      >
        <List>
          <ListItem
            button
            component={Link}
            to="/dashboard"
            onClick={handleItemClick}
            sx={{
              justifyContent: 'center',
              flexDirection: 'column',
              '&:hover': {
                bgcolor: 'rgba(0, 0, 0, 0.08)',
                '& .MuiListItemIcon-root': {
                  bgcolor: 'rgba(0, 0, 0, 0.08)',
                  borderRadius: '50%',
                },
              },
            }}
          >
            <ListItemIcon sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
              <DashboardIcon />
              <Typography variant="caption" display="block">
                Dashboard
              </Typography>
            </ListItemIcon>
          </ListItem>
          <ListItem
            button
            onMouseEnter={() => handleMouseEnter('userManagement')}
            onMouseLeave={handleMouseLeave}
            sx={{
              justifyContent: 'center',
              flexDirection: 'column',
              '&:hover': {
                bgcolor: 'rgba(0, 0, 0, 0.08)',
                '& .MuiListItemIcon-root': {
                  bgcolor: 'rgba(0, 0, 0, 0.08)',
                  borderRadius: '50%',
                },
              },
            }}
          >
            <ListItemIcon sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
              <PeopleIcon />
              <Typography variant="caption" display="block">
                User Management
              </Typography>
            </ListItemIcon>
          </ListItem>
        </List>
      </Box>

      {expandedMenu === 'userManagement' && (
        <Box
          sx={{
            width: 200,
            height: '100%',
            bgcolor: 'background.paper',
            position: 'fixed',
            left: 80,
            py: 2,
            overflowX: 'hidden',
            boxShadow: 3,
          }}
          onMouseEnter={() => handleMouseEnter('userManagement')}
          onMouseLeave={handleMouseLeave}
        >
          <List>
            <ListItem button component={Link} to="/members" onClick={handleItemClick}>
              <ListItemIcon>
                <PeopleIcon />
              </ListItemIcon>
              <ListItemText primary="Members" />
            </ListItem>
            <ListItem button component={Link} to="/reviews" onClick={handleItemClick}>
              <ListItemIcon>
                <AssignmentIcon />
              </ListItemIcon>
              <ListItemText primary="Reviews" />
            </ListItem>
            <ListItem button component={Link} to="/comments" onClick={handleItemClick}>
              <ListItemIcon>
                <CommentIcon />
              </ListItemIcon>
              <ListItemText primary="Comments" />
            </ListItem>
            <ListItem button component={Link} to="/tickets" onClick={handleItemClick}>
              <ListItemIcon>
                <MovieIcon />
              </ListItemIcon>
              <ListItemText primary="Tickets" />
            </ListItem>
            {user?.role === 1 && (
              <ListItem button components={Link} to="/admin-management" onClick={handleItemClick}>
                <ListItemIcon>
                  <PeopleIcon />
                </ListItemIcon>
                <ListItemText primary="Admin_manage "/>
              </ListItem>
            )}
          </List>
        </Box>
      )}
    </Box>
  );
};

export default Sidebar;
