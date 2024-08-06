import React, { useState, useEffect } from 'react';
import { Box, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, MenuItem, Select, FormControl } from '@mui/material';
import api from '../api';

const AdminManagement = () => {
  const [admins, setAdmins] = useState([]);

  useEffect(() => {
    fetchAdmins();
  }, []);

  const fetchAdmins = async () => {
    try {
      const response = await api.get('/auth/admin/administrators');
      setAdmins(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  const approveAdmin = async (adminId) => {
    try {
      await api.put(`/auth/admin/administrators/${adminId}/approve`);
      fetchAdmins();
    } catch (error) {
      console.error(error);
    }
  };

  const changeRole = async (adminId, role) => {
    try {
      await api.put(`/auth/admin/administrators/${adminId}/role`, { role });
      fetchAdmins();
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <Box>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Username</TableCell>
              <TableCell>Email</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Role</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {admins.map((admin) => (
              <TableRow key={admin.id}>
                <TableCell>{admin.username}</TableCell>
                <TableCell>{admin.email}</TableCell>
                <TableCell>{admin.status?.approved ? 'Approved' : 'Pending'}</TableCell>
                <TableCell>{admin.role}</TableCell>
                <TableCell>
                  {!admin.status?.approved && <Button onClick={() => approveAdmin(admin.id)}>Approve</Button>}
                  <Button onClick={() => changeRole(admin.id, 2)}>중간 관리자</Button>
                  <Button onClick={() => changeRole(admin.id, 3)}>일반 관리자</Button>
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