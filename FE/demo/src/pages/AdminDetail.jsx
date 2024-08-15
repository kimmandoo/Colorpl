import React, { useState, useEffect } from 'react';
import { Box, Typography, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@mui/material';
import { useParams } from 'react-router-dom';
import api from '../api';

const AdminDetail = () => {
  const { adminId } = useParams();
  const [admin, setAdmin] = useState(null);
  const [activities, setActivities] = useState([]); // 임시로 빈 배열로 초기화

  useEffect(() => {
    fetchAdminDetail();
    // 활동 기록 API가 없기 때문에 주석 처리
    // fetchAdminActivities();
  }, [adminId]);

  const fetchAdminDetail = async () => {
    try {
      const response = await api.get(`/auth/admin/administrators/${adminId}`);
      setAdmin(response.data);
    } catch (error) {
      console.error('Failed to fetch admin details', error);
    }
  };

  // const fetchAdminActivities = async () => {
  //   try {
  //     const response = await api.get(`/auth/admin/administrators/${adminId}/activities`);
  //     setActivities(response.data);
  //   } catch (error) {
  //     console.error('Failed to fetch admin activities', error);
  //   }
  // };

  if (!admin) {
    return <Typography>Loading...</Typography>;
  }

  return (
    <Box>
      <Typography variant="h4" gutterBottom>Admin Detail</Typography>
      <Box mb={2}>
        <Typography variant="body1">Username: {admin.username}</Typography>
        <Typography variant="body1">Email: {admin.email}</Typography>
        <Typography variant="body1">Role: {admin.role}</Typography>
        <Typography variant="body1">
          Status: {admin.status?.approved ? 'Approved' : 'Pending'}
        </Typography>
      </Box>

      <Box mt={4}>
        <Typography variant="h5" gutterBottom>활동 기록</Typography>
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>활동 날짜</TableCell>
                <TableCell>활동 내용</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {/* API가 준비되면 이 부분을 업데이트 합니다 */}
              {activities.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={2} align="center">
                    활동 기록이 없습니다.
                  </TableCell>
                </TableRow>
              ) : (
                activities.map((activity) => (
                  <TableRow key={activity.id}>
                    <TableCell>{new Date(activity.date).toLocaleString()}</TableCell>
                    <TableCell>{activity.description}</TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </TableContainer>
      </Box>
    </Box>
  );
};

export default AdminDetail;
