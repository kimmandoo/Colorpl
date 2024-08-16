import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Box, Typography, Button, TextField, Snackbar, Alert, FormControlLabel, Checkbox } from '@mui/material';
import api from '../api';

const CommentDetail = () => {
  const { comment_id } = useParams();
  const [comment, setComment] = useState(null);
  const [formData, setFormData] = useState({
    content: '',
    nickname: '',
    email: '',
    create_date: '',
    update_date: '',
    review: '',
    is_inappropriate: false,
  });
  const navigate = useNavigate();
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState('');
  const [snackbarSeverity, setSnackbarSeverity] = useState('success');

  useEffect(() => {
    const fetchCommentDetail = async () => {
      try {
        const response = await api.get(`/cs/comments/${comment_id}`);
        setComment(response.data);
        setFormData({
          content: response.data.content,
          nickname: response.data.nickname,
          email: response.data.email,
          create_date: response.data.create_date,
          update_date: response.data.update_date,
          review: response.data.review,
          is_inappropriate: response.data.is_inappropriate,
        });
      } catch (error) {
        console.error('Failed to fetch comment details:', error);
        setSnackbarMessage('Failed to fetch comment details');
        setSnackbarSeverity('error');
        setOpenSnackbar(true);
      }
    };

    fetchCommentDetail();
  }, [comment_id]);

  const handleUpdate = async () => {
    try {
      const updatedContent = formData.is_inappropriate 
        ? '규정 위반 댓글입니다.' 
        : formData.content;

      await api.patch(`/cs/comments/${comment_id}`, {
        ...formData,
        content: updatedContent
      });
      setSnackbarMessage('Comment updated successfully');
      setSnackbarSeverity('success');
      setOpenSnackbar(true);
      navigate('/comments'); // 업데이트 후 댓글 목록으로 돌아가기
    } catch (error) {
      console.error('Failed to update comment:', error);
      setSnackbarMessage('Failed to update comment');
      setSnackbarSeverity('error');
      setOpenSnackbar(true);
    }
  };

  const handleCloseSnackbar = () => setOpenSnackbar(false);

  if (!comment) return <Typography>로딩 중...</Typography>;

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-start', p: 4, maxWidth: 800, mx: 'auto' }}>
      <Typography variant="h4" mb={2}>댓글 상세 정보</Typography>
      
      <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
        <Box sx={{ flexGrow: 1 }}>
          <TextField
            label="댓글 내용"
            fullWidth
            multiline
            rows={4}
            value={formData.content}
            onChange={(e) => setFormData({ ...formData, content: e.target.value })}
            sx={{ mb: 2 }}
            disabled={formData.is_inappropriate} // 규정 위반이면 수정 불가
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
            label="리뷰 내용"
            fullWidth
            multiline
            value={formData.review || '리뷰가 없습니다.'}
            sx={{ mb: 2 }}
            disabled
          />
          <FormControlLabel
            control={
              <Checkbox
                checked={formData.is_inappropriate}
                onChange={(e) => setFormData({ ...formData, is_inappropriate: e.target.checked })}
              />
            }
            label="규정 위반 댓글로 설정"
            sx={{ mb: 2 }}
          />
        </Box>
      </Box>

      <Button variant="contained" onClick={handleUpdate} sx={{ mt: 2, mb: 2 }}>수정 완료</Button>
      <Button variant="outlined" onClick={() => navigate('/comments')} sx={{ mb: 2 }}>목록으로 돌아가기</Button>

      <Snackbar
        open={openSnackbar}
        autoHideDuration={6000}
        onClose={handleCloseSnackbar}
      >
        <Alert onClose={handleCloseSnackbar} severity={snackbarSeverity} sx={{ width: '100%' }}>
          {snackbarMessage}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default CommentDetail;
