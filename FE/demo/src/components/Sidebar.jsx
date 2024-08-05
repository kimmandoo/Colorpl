import React from 'react';
import { Box, List, ListItem, ListItemIcon, ListItemText, IconButton } from '@mui/material';
import DashboardIcon from '@mui/icons-material/Dashboard';
import GroupIcon from '@mui/icons-material/Group';
import CommentIcon from '@mui/icons-material/Comment';
import RateReviewIcon from '@mui/icons-material/RateReview';
import TicketIcon from '@mui/icons-material/ConfirmationNumber';  // 티켓 아이콘 추가
import { useNavigate } from 'react-router-dom';

const Sidebar = () => {
  const navigate = useNavigate();

  return (
    <Box
      sx={{
        width: '240px',
        flexShrink: 0,
        bgcolor: 'background.paper',
        height: '100vh',
        position: 'fixed',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
      }}
    >
      <IconButton onClick={() => navigate('/dashboard')}>
        <DashboardIcon />
      </IconButton>
      <List>
        <ListItem button onClick={() => navigate('/members')}>
          <ListItemIcon>
            <GroupIcon />
          </ListItemIcon>
          <ListItemText primary="Members" />
        </ListItem>
        <ListItem button onClick={() => navigate('/reviews')}>
          <ListItemIcon>
            <RateReviewIcon />
          </ListItemIcon>
          <ListItemText primary="Reviews" />
        </ListItem>
        <ListItem button onClick={() => navigate('/comments')}>
          <ListItemIcon>
            <CommentIcon />
          </ListItemIcon>
          <ListItemText primary="Comments" />
        </ListItem>
        <ListItem button onClick={() => navigate('/tickets')}>
          <ListItemIcon>
            <TicketIcon />
          </ListItemIcon>
          <ListItemText primary="Tickets" />
        </ListItem>
      </List>
    </Box>
  );
};

export default Sidebar;
