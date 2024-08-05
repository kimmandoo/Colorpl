import React, { useState, useEffect } from 'react';
import { Box, TextField, Button, Select, MenuItem } from '@mui/material';
import MemberList from '../components/MemberList';
import MemberModal from '../components/MemberModal';
import api from '../api';

const Members = () => {
  const [members, setMembers] = useState([]);
  const [searchParams, setSearchParams] = useState({ category: '', email: '', nickname: '', search: '' });
  const [selectedMember, setSelectedMember] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);
  const categories = ['Category1', 'Category2', 'Category3'];

  useEffect(() => {
    fetchMembers();
  }, []);

  const fetchMembers = async () => {
    try {
      const response = await api.get('/cs2/members', {
        params: { skip: 0, limit: 100 },
      });
      if (Array.isArray(response.data)) {
        setMembers(response.data);
      } else {
        setMembers([]);
      }
    } catch (error) {
      console.error(error);
      setMembers([]);
    }
  };

  const fetchMemberActivity = async (member_id) => {
    try {
      const response = await api.get(`/cs2/members/${member_id}/activity`, {
        params: { skip: 0, limit: 5 },
      });
      setSelectedMember(response.data);
      setModalOpen(true);
    } catch (error) {
      console.error(error);
    }
  };

  const onSearch = async () => {
    try {
      const response = await api.post('/cs2/members/search', {
        ...searchParams,
        skip: 0,
        limit: 100,
      });
      if (Array.isArray(response.data)) {
        setMembers(response.data);
      } else {
        setMembers([]);
      }
    } catch (error) {
      console.error(error);
      setMembers([]);
    }
  };

  return (
    <Box>
      <Box display="flex" alignItems="center" mb={2} gap={2}>
        <Select
          value={searchParams.category}
          onChange={(e) => setSearchParams({ ...searchParams, category: e.target.value })}
          displayEmpty
        >
          <MenuItem value="">None</MenuItem>
          {categories.map((category) => (
            <MenuItem key={category} value={category}>
              {category}
            </MenuItem>
          ))}
        </Select>
        <TextField
          label="Email"
          value={searchParams.email}
          onChange={(e) => setSearchParams({ ...searchParams, email: e.target.value })}
          margin="normal"
        />
        <TextField
          label="Nickname"
          value={searchParams.nickname}
          onChange={(e) => setSearchParams({ ...searchParams, nickname: e.target.value })}
          margin="normal"
        />
        <TextField
          label="Search"
          value={searchParams.search}
          onChange={(e) => setSearchParams({ ...searchParams, search: e.target.value })}
          margin="normal"
          onKeyPress={(e) => {
            if (e.key === 'Enter') {
              onSearch();
            }
          }}
        />
        <Button onClick={onSearch} variant="contained" color="primary">
          Search
        </Button>
      </Box>
      <MemberList members={members} onMemberClick={fetchMemberActivity} />
      <MemberModal open={modalOpen} onClose={() => setModalOpen(false)} member={selectedMember} />
    </Box>
  );
};

export default Members;
