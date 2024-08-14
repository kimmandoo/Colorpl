import React, { useState } from 'react';
import { Box, Button, Typography } from '@mui/material';
import api from '../api';

const ScheduleImageUpdate = ({ scheduleId, onClose }) => {
  const [file, setFile] = useState(null);

  const handleFileChange = (event) => {
    setFile(event.target.files[0]);
  };

  const handleImageUpdate = async () => {
    if (!file) {
      alert('Please select a file first');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);

    try {
      await api.patch(`/cs/schedules/${scheduleId}/image`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      alert('Image updated successfully');
      onClose(); // Close the modal after successful update
    } catch (error) {
      console.error('Failed to update image:', error);
      alert('Failed to update image');
    }
  };

  return (
    <Box>
      <Typography variant="h6">이미지 업데이트</Typography>
      <input type="file" onChange={handleFileChange} />
      <Button variant="contained" onClick={handleImageUpdate} sx={{ mt: 2 }}>
        업데이트
      </Button>
    </Box>
  );
};

export default ScheduleImageUpdate;
