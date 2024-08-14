import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Box, Typography, Button, TextField, Snackbar, Alert } from '@mui/material';
import api from '../api';

const ReservationDetail = () => {
  const { reservation_id } = useParams();
  const [reservation, setReservation] = useState(null);
  const [formData, setFormData] = useState({
    nickname: '',
    member_id: '',
    profile: '',
    create_date: '',
    update_date: '',
    reserve_date: '',
    reserve_amount: '',
    reserve_comment: '',
    is_refunded: false,
    seat_col: '',
    seat_row: '',
    show_detail_name: '',
    hall_name: ''
  });
  const navigate = useNavigate();
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState('');
  const [snackbarSeverity, setSnackbarSeverity] = useState('success');

  useEffect(() => {
    const fetchReservationDetail = async () => {
      try {
        const response = await api.get(`/vm/reservations/${reservation_id}`);
        setReservation(response.data);
        setFormData({
          nickname: response.data.nickname,
          member_id: response.data.member_id,
          profile: response.data.profile,
          create_date: response.data.create_date,
          update_date: response.data.update_date,
          reserve_date: response.data.reserve_date,
          reserve_amount: response.data.reserve_amount,
          reserve_comment: response.data.reserve_comment,
          is_refunded: response.data.is_refunded,
          seat_col: response.data.seat_col,
          seat_row: response.data.seat_row,
          show_detail_name: response.data.show_detail_name,
          hall_name: response.data.hall_name,
        });
      } catch (error) {
        console.error('Failed to fetch reservation details:', error);
        setSnackbarMessage('Failed to fetch reservation details');
        setSnackbarSeverity('error');
        setOpenSnackbar(true);
      }
    };

    fetchReservationDetail();
  }, [reservation_id]);

  const handleUpdate = async () => {
    try {
      await api.patch(`/vm/reservations/${reservation_id}`, formData);
      setSnackbarMessage('Reservation updated successfully');
      setSnackbarSeverity('success');
      setOpenSnackbar(true);
      navigate('/reservations'); // 업데이트 후 예약 목록으로 돌아가기
    } catch (error) {
      console.error('Failed to update reservation:', error);
      setSnackbarMessage('Failed to update reservation');
      setSnackbarSeverity('error');
      setOpenSnackbar(true);
    }
  };

  const handleCloseSnackbar = () => setOpenSnackbar(false);

  if (!reservation) return <Typography>로딩 중...</Typography>;

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-start', p: 4, maxWidth: 800, mx: 'auto' }}>
      <Typography variant="h4" mb={2}>예약 상세 정보</Typography>
      
      <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
        <Box sx={{ flexGrow: 1 }}>
          <TextField
            label="닉네임"
            fullWidth
            value={formData.nickname}
            sx={{ mb: 2 }}
            disabled
          />
          <TextField
            label="멤버 ID"
            fullWidth
            value={formData.member_id}
            sx={{ mb: 2 }}
            disabled
          />
          <TextField
            label="프로필"
            fullWidth
            value={formData.profile || '프로필 정보가 없습니다.'}
            sx={{ mb: 2 }}
            disabled
          />
          <TextField
            label="생성일"
            fullWidth
            value={formData.create_date}
            sx={{ mb: 2 }}
            disabled
          />
          <TextField
            label="수정일"
            fullWidth
            value={formData.update_date || '수정된 정보가 없습니다.'}
            sx={{ mb: 2 }}
            disabled
          />
          <TextField
            label="예약일"
            fullWidth
            value={formData.reserve_date}
            sx={{ mb: 2 }}
            disabled
          />
          <TextField
            label="예약 금액"
            fullWidth
            value={formData.reserve_amount}
            onChange={(e) => setFormData({ ...formData, reserve_amount: e.target.value })}
            sx={{ mb: 2 }}
          />
          <TextField
            label="예약 코멘트"
            fullWidth
            value={formData.reserve_comment}
            onChange={(e) => setFormData({ ...formData, reserve_comment: e.target.value })}
            sx={{ mb: 2 }}
          />
          <TextField
            label="환불 여부"
            fullWidth
            value={formData.is_refunded ? '환불 완료' : '환불 미완료'}
            sx={{ mb: 2 }}
            disabled
          />
          <TextField
            label="좌석 열"
            fullWidth
            value={formData.seat_col}
            sx={{ mb: 2 }}
            disabled
          />
          <TextField
            label="좌석 번호"
            fullWidth
            value={formData.seat_row}
            sx={{ mb: 2 }}
            disabled
          />
          <TextField
            label="상세 쇼 이름"
            fullWidth
            value={formData.show_detail_name}
            sx={{ mb: 2 }}
            disabled
          />
          <TextField
            label="홀 이름"
            fullWidth
            value={formData.hall_name}
            sx={{ mb: 2 }}
            disabled
          />
        </Box>
      </Box>

      <Button variant="contained" onClick={handleUpdate} sx={{ mt: 2, mb: 2 }}>수정 완료</Button>
      <Button variant="outlined" onClick={() => navigate('/reservations')} sx={{ mb: 2 }}>목록으로 돌아가기</Button>

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

export default ReservationDetail;
