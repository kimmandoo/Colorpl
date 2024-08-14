import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Box, Typography, Button, Modal, TextField, Avatar } from '@mui/material';
import api from '../api';

const ReviewDetail = () => {
  const { review_id } = useParams();
  const [review, setReview] = useState(null);
  const [formData, setFormData] = useState({
    review_content: '',
    review_emotion: 0,
    is_spoiler: false,
    schedule_name: '',
    schedule_category: '',
    create_date: '',
    update_date: '',
    nickname: '',
    email: ''
  });
  const navigate = useNavigate();
  const [openModal, setOpenModal] = useState(false);

  useEffect(() => {
    const fetchReviewDetail = async () => {
      try {
        const response = await api.get(`/cs/reviews/${review_id}`);
        setReview(response.data);
        setFormData({
          review_filename: response.data.review_filename,
          review_content: response.data.review_content,
          review_emotion: response.data.review_emotion,
          is_spoiler: response.data.is_spoiler,
          schedule_name: response.data.schedule_name,
          schedule_category: response.data.schedule_category,
          create_date: response.data.create_date,
          update_date: response.data.update_date,
          nickname: response.data.nickname,
          email: response.data.email
        });
      } catch (error) {
        console.error('Failed to fetch review details:', error);
      }
    };

    fetchReviewDetail();
  }, [review_id]);

  const handleUpdate = async () => {
    try {
      await api.patch(`/cs/reviews/${review_id}`, formData);
      navigate('/reviews'); // 업데이트 후 리뷰 목록으로 돌아가기
    } catch (error) {
      console.error('리뷰 정보 업데이트에 실패했습니다.', error);
    }
  };

  const handleOpenModal = () => setOpenModal(true);
  const handleCloseModal = () => setOpenModal(false);
  

  if (!review) return <Typography>로딩 중...</Typography>;

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-start', p: 4, maxWidth: 1000, mx: 'auto' }}>
      <Typography variant="h4" mb={2}>리뷰 상세 정보</Typography>
      
      <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
        <Box sx={{ flexGrow: 1 }}>
          <TextField
            label="리뷰 내용"
            fullWidth
            multiline
            rows={4}
            value={formData.review_content}
            onChange={(e) => setFormData({ ...formData, review_content: e.target.value })}
            sx={{ mb: 2 }}
          />
          <TextField
            label="감정"
            fullWidth
            type="number"
            value={formData.review_emotion}
            onChange={(e) => setFormData({ ...formData, review_emotion: e.target.value })}
            sx={{ mb: 2 }}
          />
          <TextField
            label="스포일러 여부"
            fullWidth
            value={formData.is_spoiler ? 'Yes' : 'No'}
            onChange={(e) => setFormData({ ...formData, is_spoiler: e.target.value === 'Yes' })}
            sx={{ mb: 2 }}
          />
          <TextField
            label="스케줄 이름"
            fullWidth
            value={formData.schedule_name}
            onChange={(e) => setFormData({ ...formData, schedule_name: e.target.value })}
            sx={{ mb: 2 }}
            disabled
          />
          <TextField
            label="카테고리"
            fullWidth
            value={formData.schedule_category}
            onChange={(e) => setFormData({ ...formData, schedule_category: e.target.value })}
            sx={{ mb: 2 }}
            disabled
          />
          <TextField
            label="작성일"
            fullWidth
            value={formData.create_date}
            sx={{ mb: 2 }}
            disabled
          />
          <TextField
            label="수정일"
            fullWidth
            value={formData.update_date}
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
          src={`https://i11d109.p.ssafy.io/images/${formData.review_filename}`} 
          alt="Review Image" 
          sx={{ width: 300, height: 300, mr: 3 }}
        />
      </Box>

      <Button variant="contained" onClick={handleUpdate} sx={{ mt: 2, mb: 2 }}>수정 완료</Button>
      <Button variant="outlined" onClick={() => navigate('/reviews')} sx={{ mb: 2 }}>목록으로 돌아가기</Button>

    </Box>
  );
};

export default ReviewDetail;
