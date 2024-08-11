import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Box, Typography, Button, TextField, Avatar } from '@mui/material';
import api from '../api';

const MemberDetail = () => {
  const { member_id } = useParams();
  const [member, setMember] = useState(null);
  const [formData, setFormData] = useState({
    nickname: '',
    email: '',
    profile: '',
    type: ''
    // 필요한 다른 필드들 추가
  });
  const navigate = useNavigate();

  useEffect(() => {
    const fetchMember = async () => {
      try {
        const response = await api.get(`/cs/members/${member_id}`);
        setMember(response.data);
        setFormData({
          nickname: response.data.nickname,
          email: response.data.email,
          profile: response.data.profile, // 프로필 이미지 추가
          type: response.data.type,
          // 필요한 다른 필드들 추가
        });
      } catch (error) {
        console.error('회원 정보를 불러오는데 실패했습니다.', error);
      }
    };

    fetchMember();
  }, [member_id]);

  const handleUpdate = async () => {
    try {
      await api.patch(`/cs/members/${member_id}`, formData);
      navigate('/members'); // 업데이트 후 멤버 목록으로 돌아가기
    } catch (error) {
      console.error('회원 정보 업데이트에 실패했습니다.', error);
    }
  };

  if (!member) return <Typography>로딩 중...</Typography>;

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-start', p: 4, maxWidth: 800, mx: 'auto' }}>
      <Typography variant="h4" mb={2}>회원 상세 정보</Typography>
      
      <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
        <Box sx={{ flexGrow: 1 }}>
          <TextField
            label="닉네임"
            fullWidth
            value={formData.nickname}
            onChange={(e) => setFormData({ ...formData, nickname: e.target.value })}
            sx={{ mb: 2 }}
          />
          <TextField
            label="이메일"
            fullWidth
            value={formData.email}
            onChange={(e) => setFormData({ ...formData, email: e.target.value })}
            sx={{ mb: 2 }}
          />
          <TextField
            label="타입"
            fullWidth
            value={formData.type}
            onChange={(e) => setFormData({ ...formData, type: e.target.value })}
            sx={{ mb: 2 }}
          />
          <TextField
            label="프로필"
            fullWidth
            value={formData.profile}
            onChange={(e) => setFormData({ ...formData, profile: e.target.value })}
            sx={{ mb: 2 }}
          />
          {/* 필요한 다른 필드들 추가 */}
        </Box>
        <Avatar 
          src={`https://i11d109.p.ssafy.io/images/${formData.profile}`}
          alt={formData.nickname} 
          sx={{ width: 300, height: 300, mr: 3 }}
        />
      </Box>

      <Button variant="contained" onClick={handleUpdate} sx={{ mt: 2, mb: 2 }}>수정 완료</Button>
      <Button variant="outlined" onClick={() => navigate('/members')}>목록으로 돌아가기</Button>
    </Box>
  );
};

export default MemberDetail;
