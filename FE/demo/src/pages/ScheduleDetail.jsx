import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Box, Typography, Button, Modal, TextField, Avatar } from '@mui/material';
import api from '../api';
import ScheduleImageUpdate from './ScheduleImageUpdate';

const ScheduleDetail = () => {
  const { schedule_id } = useParams();
  const [schedule, setSchedule] = useState(null);
  const [formData, setFormData] = useState({
    schedule_name: '',
    schedule_category: '',
    schedule_date_time: '',
    schedule_location: '',
    schedule_seat: '',
    schedule_image: '',
    review_written: false,
    is_reserved: false,
    nickname: '',
    email: ''
  });
  const navigate = useNavigate();
  const [openModal, setOpenModal] = useState(false);

  useEffect(() => {
    const fetchScheduleDetail = async () => {
      try {
        const response = await api.get(`/cs/schedules/${schedule_id}`);
        setSchedule(response.data);
        setFormData({
          schedule_name: response.data.schedule_name,
          schedule_category: response.data.schedule_category,
          schedule_date_time: response.data.schedule_date_time,
          schedule_location: response.data.schedule_location,
          schedule_seat: response.data.schedule_seat,
          schedule_image: response.data.schedule_image,
          review_written: response.data.review_written,
          is_reserved: response.data.is_reserved,
          nickname: response.data.nickname,
          email: response.data.email
        });
      } catch (error) {
        console.error('Failed to fetch schedule details:', error);
      }
    };

    fetchScheduleDetail();
  }, [schedule_id]);

  const handleUpdate = async () => {
    try {
      await api.patch(`/cs/schedules/${schedule_id}`, formData);
      navigate('/schedules'); // 업데이트 후 스케줄 목록으로 돌아가기
    } catch (error) {
      console.error('스케줄 정보 업데이트에 실패했습니다.', error);
    }
  };

  const handleOpenModal = () => setOpenModal(true);
  const handleCloseModal = () => setOpenModal(false);

  if (!schedule) return <Typography>로딩 중...</Typography>;

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-start', p: 4, maxWidth: 1000, mx: 'auto' }}>
      <Typography variant="h4" mb={2}>스케줄 상세 정보</Typography>
      
      <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
        <Box sx={{ flexGrow: 1 }}>
          <TextField
            label="스케줄 이름"
            fullWidth
            value={formData.schedule_name}
            onChange={(e) => setFormData({ ...formData, schedule_name: e.target.value })}
            sx={{ mb: 2 }}
          />
          <TextField
            label="카테고리"
            fullWidth
            value={formData.schedule_category}
            onChange={(e) => setFormData({ ...formData, schedule_category: e.target.value })}
            sx={{ mb: 2 }}
          />
          <TextField
            label="등록 시간"
            fullWidth
            value={formData.schedule_date_time}
            onChange={(e) => setFormData({ ...formData, schedule_date_time: e.target.value })}
            sx={{ mb: 2 }}
          />
          <TextField
            label="위치"
            fullWidth
            value={formData.schedule_location}
            onChange={(e) => setFormData({ ...formData, schedule_location: e.target.value })}
            sx={{ mb: 2 }}
          />
          <TextField
            label="좌석"
            fullWidth
            value={formData.schedule_seat}
            onChange={(e) => setFormData({ ...formData, schedule_seat: e.target.value })}
            sx={{ mb: 2 }}
          />
          <TextField
            label="리뷰 작성 여부"
            fullWidth
            value={formData.review_written ? 'Yes' : 'No'}
            sx={{ mb: 2 }}
            disabled
          />
          <TextField
            label="예매 여부"
            fullWidth
            value={formData.is_reserved ? 'Yes' : 'No'}
            sx={{ mb: 2 }}
            disabled
          />
          <TextField
            label="닉네임"
            fullWidth
            value={formData.nickname}
            sx={{ mb: 2 }}
            disabled
          />
          <TextField
            label="이메일"
            fullWidth
            value={formData.email}
            sx={{ mb: 2 }}
            disabled
          />
        </Box>
        <Avatar 
          src={formData.schedule_image.startsWith('http') 
            ? formData.schedule_image 
            : `https://i11d109.p.ssafy.io/images/${formData.schedule_image}`} 
          alt={formData.schedule_name} 
          sx={{ width: 300, height: 300, mr: 3 }}
        />
      </Box>

      <Button variant="contained" onClick={handleUpdate} sx={{ mt: 2, mb: 2 }}>수정 완료</Button>
      <Button variant="outlined" onClick={() => navigate('/schedules')} sx={{ mb: 2 }}>목록으로 돌아가기</Button>

      <Modal open={openModal} onClose={handleCloseModal}>
        <Box sx={{ position: 'absolute', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', width: 400, bgcolor: 'background.paper', p: 4, boxShadow: 24 }}>
          <Button onClick={handleCloseModal} sx={{ position: 'absolute', top: 8, right: 8 }}>X</Button>
          <ScheduleImageUpdate scheduleId={schedule_id} onClose={handleCloseModal} />
        </Box>
      </Modal>
    </Box>
  );
};

export default ScheduleDetail;
