import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Box, Typography, List, ListItem, ListItemText, CircularProgress, Paper } from '@mui/material';
import api from '../api';

const TheaterHallPage = () => {
  const { theaterId } = useParams(); // URL에서 theaterId를 가져옵니다
  const [theater, setTheater] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchTheater = async () => {
      try {
        if (!theaterId) {
          throw new Error('Theater ID is missing');
        }

        console.log('Fetching theater data for ID:', theaterId); // 디버깅을 위한 로그
        
        // 극장 정보와 연결된 홀 정보를 모두 가져옵니다.
        const response = await api.get(`/vm/theaters/${theaterId}`);
        setTheater(response.data);
      } catch (error) {
        console.error('Error fetching theater information', error);
      } finally {
        setLoading(false);
      }
    };

    fetchTheater();
  }, [theaterId]);

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh">
        <CircularProgress />
      </Box>
    );
  }

  if (!theater) {
    return (
      <Box textAlign="center" mt={4}>
        <Typography variant="h4">극장 정보를 찾을 수 없습니다</Typography>
      </Box>
    );
  }

  return (
    <Box p={4}>
      <Paper elevation={3} sx={{ padding: 4 }}>
        <Typography variant="h4" gutterBottom>{theater.theater_name}</Typography>
        <Typography variant="subtitle1" gutterBottom>{theater.theater_address}</Typography>
        <Typography variant="subtitle2" gutterBottom>
          위치: {theater.theater_latitude}, {theater.theater_longitude}
        </Typography>
        
        <Typography variant="h5" gutterBottom mt={4}>
          연결된 홀 정보
        </Typography>
        {theater.halls && theater.halls.length === 0 ? (
          <Typography variant="body1">이 극장에 연결된 홀이 없습니다</Typography>
        ) : (
          <List>
            {theater.halls.map((hall) => (
              <ListItem key={hall.hall_id}>
                <ListItemText primary={hall.hall_name} />
              </ListItem>
            ))}
          </List>
        )}
      </Paper>
    </Box>
  );
};

export default TheaterHallPage;
