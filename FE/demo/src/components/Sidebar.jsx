import React from 'react';
import { Box, List, ListItem, ListItemIcon, ListItemText, Typography } from '@mui/material';
import { People as PeopleIcon, Comment as CommentIcon, Movie as MovieIcon, Dashboard as DashboardIcon, Assignment as AssignmentIcon, Store as StoreIcon, TheaterComedy as TheaterComedyIcon, ManageAccounts as ManageAccountsIcon, ShoppingCart as ShoppingCartIcon } from '@mui/icons-material';
import { Link } from 'react-router-dom';

const Sidebar = ({ user, expandedMenu, setExpandedMenu }) => {

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
                대시보드
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
                사용자 <br /> 관리
              </Typography>
            </ListItemIcon>
          </ListItem>
          <ListItem
            button
            onMouseEnter={() => handleMouseEnter('salesManagement')}
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
              <StoreIcon />
              <Typography variant="caption" display="block">
                판매 <br /> 관리
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
              <ListItemText primary="유저 관리" />
            </ListItem>
            <ListItem button component={Link} to="/schedules" onClick={handleItemClick}>
              <ListItemIcon>
                <MovieIcon />
              </ListItemIcon>
              <ListItemText primary="스케줄 관리" />
            </ListItem>
            <ListItem button component={Link} to="/reviews" onClick={handleItemClick}>
              <ListItemIcon>
                <AssignmentIcon />
              </ListItemIcon>
              <ListItemText primary="리뷰 관리" />
            </ListItem>
            <ListItem button component={Link} to="/comments" onClick={handleItemClick}>
              <ListItemIcon>
                <CommentIcon />
              </ListItemIcon>
              <ListItemText primary="댓글 관리" />
            </ListItem>
            <ListItem button component={Link} to="/reservations" onClick={handleItemClick}>
              <ListItemIcon>
                <ShoppingCartIcon />
              </ListItemIcon>
              <ListItemText primary="예매 관리" />
            </ListItem>
            {user?.role === 1 && (
              <ListItem button component={Link} to="/admin-management" onClick={handleItemClick}>
                <ListItemIcon>
                  <ManageAccountsIcon />
                </ListItemIcon>
                <ListItemText primary="관리자 관리" />
              </ListItem>
            )}
          </List>
        </Box>
      )}

      {expandedMenu === 'salesManagement' && (
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
          onMouseEnter={() => handleMouseEnter('salesManagement')}
          onMouseLeave={handleMouseLeave}
        >
          <List>
            <ListItem button component={Link} to="/register-show" onClick={handleItemClick}>
              <ListItemIcon>
                <TheaterComedyIcon />
              </ListItemIcon>
              <ListItemText primary="공연 등록" />
            </ListItem>
            <ListItem button onClick={handleItemClick}>
              <ListItemIcon>
                <MovieIcon />
              </ListItemIcon>
              <ListItemText primary="공연 관리" />
            </ListItem>
            <ListItem button component={Link} to="/theaters" onClick={handleItemClick}>
              <ListItemIcon>
                <StoreIcon />
              </ListItemIcon>
              <ListItemText primary="극장 관리" />
            </ListItem>
          </List>
        </Box>
      )}
    </Box>
  );
};

export default Sidebar;
