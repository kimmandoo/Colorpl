import React, { useState, useEffect } from 'react';
import { Box, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, Select, MenuItem, FormControl } from '@mui/material';
import api from '../api';
import { useNavigate } from 'react-router-dom';

const AdminManagement = ({ user }) => {
  const [admins, setAdmins] = useState([]);
  const [selectedRole, setSelectedRole] = useState({});
  const [selectedApproval, setSelectedApproval] = useState({});
  const navigate = useNavigate();

  useEffect(() => {
    if (!user || user.role !== 1) {
      navigate('/dashboard'); // 권한이 없는 경우 대시보드로 리다이렉트
    } else {
      fetchAdmins();
    }
  }, [user, navigate]);

  const fetchAdmins = async () => {
    try {
      const response = await api.get('/auth/admin/administrators');
      const filteredAdmins = response.data.filter(admin => admin.role !== 1); // 슈퍼 관리자는 필터링
      setAdmins(filteredAdmins);
    } catch (error) {
      console.error('관리자를 가져오는데 실패했습니다.', error);
    }
  };

  const handleNameClick = (adminId) => {
    navigate(`/admin-detail/${adminId}`);
  };

  const handleEmailClick = (adminId) => {
    navigate(`/admin-detail/${adminId}`);
  };

  // 역할 변경과 승인 관련 핸들러들
  const handleRoleChange = (adminId, role) => {
    setSelectedRole((prevState) => ({
      ...prevState,
      [adminId]: role,
    }));
  };

  const handleApprovalChange = (adminId, approvalStatus) => {
    setSelectedApproval((prevState) => ({
      ...prevState,
      [adminId]: approvalStatus,
    }));
  };

  const updateRole = async (adminId) => {
    try {
      await api.put('/auth/admin/administrators/role', {
        admin_id: adminId,
        role: selectedRole[adminId],
      });
      fetchAdmins();
    } catch (error) {
      console.error('역할 업데이트에 실패했습니다.', error);
    }
  };

  const approveAdmin = async (adminId) => {
    try {
      await api.put('/auth/admin/administrators/approve', {
        admin_id: adminId,
      });
      fetchAdmins();
    } catch (error) {
      console.error('관리자 승인을 실패했습니다.', error);
    }
  };

  const requestDelete = async (adminId) => {
    try {
      await api.post('/auth/admin/delete_request', {
        admin_id: adminId,
      });
      fetchAdmins();
    } catch (error) {
      console.error('삭제 요청에 실패했습니다.', error);
    }
  };

  const approveDelete = async (adminId) => {
    try {
      await api.post('/auth/admin/approve_delete', {
        admin_id: adminId,
        token: localStorage.getItem('token'),
      });
      fetchAdmins();
    } catch (error) {
      console.error('삭제 승인에 실패했습니다.', error);
    }
  };

  return (
    <Box>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>이름</TableCell>
              <TableCell>이메일</TableCell>
              <TableCell>상태</TableCell>
              <TableCell>역할</TableCell>
              <TableCell>작업</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {admins.map((admin) => (
              <TableRow key={admin.id}>
                <TableCell 
                  style={{ cursor: 'pointer', textDecoration: 'underline', color: 'blue' }}
                  onClick={() => handleNameClick(admin.id)}
                >
                  {admin.username}
                </TableCell>
                <TableCell 
                  style={{ cursor: 'pointer', textDecoration: 'underline', color: 'blue' }}
                  onClick={() => handleEmailClick(admin.id)}
                >
                  {admin.email}
                </TableCell>
                <TableCell>
                  <FormControl fullWidth>
                    <Select
                      value={
                        admin.status?.deleted
                          ? '비활성화됨'
                          : admin.status?.approved
                          ? '승인됨'
                          : '대기 중'
                      }
                      onChange={(e) => handleApprovalChange(admin.id, e.target.value)}
                      disabled={admin.status?.approved && !admin.status?.delete_requested_at} // 승인된 상태에서는 비활성화 요청이 있을 때만 변경 가능
                    >
                      {admin.status?.deleted ? (
                        <MenuItem value="비활성화됨">비활성화됨</MenuItem>
                      ) : admin.status?.approved ? (
                        [
                          <MenuItem key="approved" value="승인됨">승인됨</MenuItem>,
                          <MenuItem key="delete_request" value="비활성화 요청">비활성화 요청</MenuItem>
                        ]
                      ) : (
                        [
                          <MenuItem key="pending" value="대기 중">대기 중</MenuItem>,
                          <MenuItem key="approved" value="승인됨">승인됨</MenuItem>
                        ]
                      )}
                    </Select>
                  </FormControl>
                </TableCell>
                <TableCell>
                  <FormControl fullWidth>
                    <Select
                      value={selectedRole[admin.id] !== undefined ? selectedRole[admin.id] : admin.role}
                      onChange={(e) => handleRoleChange(admin.id, e.target.value)}
                    >
                      <MenuItem value={2}>중간 관리자</MenuItem>
                      <MenuItem value={3}>일반 관리자</MenuItem>
                    </Select>
                  </FormControl>
                </TableCell>
                <TableCell>
                  {!admin.status?.approved && (
                    <Button
                      variant="contained"
                      color="primary"
                      onClick={() => approveAdmin(admin.id)}
                      disabled={selectedApproval[admin.id] === '승인됨'}
                    >
                      승인
                    </Button>
                  )}
                  {admin.status?.approved && (
                    <Button
                      variant="contained"
                      color="secondary"
                      onClick={() => approveDelete(admin.id)}
                      disabled={!admin.status?.delete_requested_at}
                    >
                      비활성화 승인
                    </Button>
                  )}
                  <Button
                    variant="contained"
                    color="secondary"
                    onClick={() => updateRole(admin.id)}
                    disabled={selectedRole[admin.id] === undefined || selectedRole[admin.id] === admin.role}
                  >
                    역할 변경
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default AdminManagement;
