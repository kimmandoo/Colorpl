import React, { useState } from 'react';
import { Box, TextField, Button, Typography, List, ListItem, ListItemText, Paper } from '@mui/material';
import api from '../api';

const TheaterSearchPage = ({ onHallSelect }) => {
  const [theaterName, setTheaterName] = useState('');
  const [theaters, setTheaters] = useState([]);
  const [halls, setHalls] = useState([]);

  const searchTheaters = async () => {
    try {
      const response = await api.post('/vm/theaters/search', { theater_name: theaterName });
      setTheaters(response.data);
    } catch (error) {
      console.error('Error searching theaters', error);
    }
  };

  const handleTheaterClick = async (theater) => {
    try {
      const response = await api.get(`/vm/theaters/${theater.theater_id}/halls`);
      setHalls(response.data);
    } catch (error) {
      console.error('Error fetching halls', error);
    }
  };

  const handleHallClick = (hall) => {
    onHallSelect(hall.hall_id);
  };

  return (
    <Box 
      display="flex" 
      alignItems="flex-start" 
      justifyContent="space-between" 
      minHeight="100vh" 
      p={3}
      gap={2}
    >
      {/* 검색창 섹션 */}
      <Paper elevation={3} sx={{ p: 2, width: '20%' }}>
        <Typography variant="h5" gutterBottom>
          극장 검색
        </Typography>
        <TextField
          variant="outlined"
          label="극장 이름"
          value={theaterName}
          onChange={(e) => setTheaterName(e.target.value)}
          placeholder="극장 이름을 입력하세요"
          fullWidth
          sx={{ mb: 2 }}
        />
        <Button 
          variant="contained" 
          onClick={searchTheaters} 
          fullWidth
        >
          검색
        </Button>
      </Paper>

      {/* 극장 결과 섹션 */}
      <Paper elevation={3} sx={{ p: 2, width: '35%' }}>
        <Typography variant="h5" gutterBottom>
          검색 결과
        </Typography>
        <List>
          {theaters.map(theater => (
            <ListItem 
              button 
              key={theater.theater_id} 
              onClick={() => handleTheaterClick(theater)}
            >
              <ListItemText primary={theater.theater_name} />
            </ListItem>
          ))}
        </List>
      </Paper>

      {/* 홀 결과 섹션 */}
      <Paper elevation={3} sx={{ p: 2, width: '35%' }}>
        <Typography variant="h5" gutterBottom>
          홀 선택
        </Typography>
        <List>
          {halls.map(hall => (
            <ListItem 
              button 
              key={hall.hall_id} 
              onClick={() => handleHallClick(hall)}
            >
              <ListItemText primary={hall.HALL_NAME} />
            </ListItem>
          ))}
        </List>
      </Paper>
    </Box>
  );
};

export default TheaterSearchPage;
