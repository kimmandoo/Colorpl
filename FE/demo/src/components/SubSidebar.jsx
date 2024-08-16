// components/SubSidebar.jsx
import React from 'react';
import { Drawer, List, ListItem, ListItemIcon, ListItemText, IconButton } from '@mui/material';
import PeopleIcon from '@mui/icons-material/People';
import CommentIcon from '@mui/icons-material/Comment';
import RateReviewIcon from '@mui/icons-material/RateReview';
import ConfirmationNumberIcon from '@mui/icons-material/ConfirmationNumber'; // 티켓 아이콘 추가
import CloseIcon from '@mui/icons-material/Close';
import { useNavigate } from 'react-router-dom';

const SubSidebar = ({ sidebarExpanded, setSidebarExpanded }) => {
  const navigate = useNavigate();

  const handleNavigate = (path) => {
    navigate(path);
    setSidebarExpanded(false); // 페이지 전환 시 사이드바 축소
  };

  return (
    <Drawer
      variant="persistent"
      open={sidebarExpanded}
      sx={{ width: 240, flexShrink: 0, zIndex: 1200 }}
      PaperProps={{ style: { width: 240 } }}
    >
      <List>
        <ListItem>
          <ListItemIcon>
            <IconButton onClick={() => setSidebarExpanded(false)}>
              <CloseIcon />
            </IconButton>
          </ListItemIcon>
        </ListItem>
        <ListItem button onClick={() => handleNavigate('/members')}>
          <ListItemIcon sx={{ color: 'black' }}>
            <PeopleIcon />
          </ListItemIcon>
          <ListItemText primary="Members" />
        </ListItem>
        <ListItem button onClick={() => handleNavigate('/reviews')}>
          <ListItemIcon sx={{ color: 'black' }}>
            <RateReviewIcon />
          </ListItemIcon>
          <ListItemText primary="Reviews" />
        </ListItem>
        <ListItem button onClick={() => handleNavigate('/comments')}>
          <ListItemIcon sx={{ color: 'black' }}>
            <CommentIcon />
          </ListItemIcon>
          <ListItemText primary="Comments" />
        </ListItem>
        <ListItem button onClick={() => handleNavigate('/tickets')}>
          <ListItemIcon sx={{ color: 'black' }}>
            <ConfirmationNumberIcon />
          </ListItemIcon>
          <ListItemText primary="Tickets" />
        </ListItem>
      </List>
    </Drawer>
  );
};

export default SubSidebar;
