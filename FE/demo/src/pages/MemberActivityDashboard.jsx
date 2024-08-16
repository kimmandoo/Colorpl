// src/pages/MemberActivityDashboard.jsx

import React, { useState, useEffect } from 'react';
import { Box, Paper, Typography, CircularProgress } from '@mui/material';
import api from '../api';

const MemberActivityDashboard = () => {
  const [loading, setLoading] = useState(true);
  const [memberActivities, setMemberActivities] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchMemberActivities = async () => {
      try {
        const response = await api.get('/cs/members/activity', {
          params: {
            skip: 0,
            limit: 5, // 대시보드에 표시할 회원 활동의 수
          },
        });
        setMemberActivities(response.data);
      } catch (error) {
        setError('회원 활동 데이터를 불러오는 중 오류가 발생했습니다.');
        console.error('Error fetching member activities:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchMemberActivities();
  }, []);

  if (loading) {
    return <CircularProgress />;
  }

  if (error) {
    return <Typography color="error">{error}</Typography>;
  }

  return (
    <Paper elevation={3} sx={{ p: 2 }}>
      <Typography variant="h6" gutterBottom>
        회원 활동 요약
      </Typography>
      {memberActivities.length === 0 ? (
        <Typography>표시할 데이터가 없습니다.</Typography>
      ) : (
        memberActivities.map((activity) => (
          <Box key={activity.member_id} sx={{ mb: 2 }}>
            <Typography variant="subtitle1">
              닉네임: {activity.nickname} (이메일: {activity.email})
            </Typography>
            <Typography variant="body2">
              생성일: {new Date(activity.create_date).toLocaleDateString()}
            </Typography>
            <Typography variant="body2">
              스케줄 수: {activity.schedules_count} | 리뷰 수: {activity.reviews_count} | 댓글 수: {activity.comments_count} | 예약 수: {activity.reservations_count}
            </Typography>
          </Box>
        ))
      )}
    </Paper>
  );
};

export default MemberActivityDashboard;
