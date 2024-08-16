import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Table, TableBody, TableCell, TableHead, TableRow, Box, Button, TextField,
  FormControl, InputLabel, Select, MenuItem, Pagination, Snackbar, Alert, Typography,
  Chip, Paper
} from '@mui/material';
import api from '../api';

const Members = () => {
  const [members, setMembers] = useState([]);
  const [totalMembers, setTotalMembers] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const membersPerPage = 10;
  const maxPages = 100;
  const [error, setError] = useState('');
  const [open, setOpen] = useState(false);
  const [selectedOptions, setSelectedOptions] = useState({});
  const [loading, setLoading] = useState(false);

  const [searchParams, setSearchParams] = useState({
    nickname: '',
    email: '',
    create_date_from: '',
    create_date_to: '',
    category: []
  });

  const navigate = useNavigate();

  const cleanSearchParams = (params) => {
    const cleanedParams = { ...params };
    Object.keys(cleanedParams).forEach((key) => {
      if (!cleanedParams[key] || cleanedParams[key].length === 0) {
        delete cleanedParams[key];
      }
    });
    return cleanedParams;
  };

  const fetchActivityData = useCallback(async (params) => {
    setLoading(true);
    try {
      const response = await api.get('/cs/members/activity', { params });
      if (Array.isArray(response.data)) {
        setMembers(response.data);
        setTotalMembers(response.data.length > 0 ? response.data[0].total_count : 0);
      } else {
        setMembers([]);
        setTotalMembers(0);
      }
    } catch (error) {
      console.error('회원 정보를 불러오는데 실패했습니다.', error);
      setError('회원 정보를 불러오는데 실패했습니다.');
      setOpen(true);
    } finally {
      setLoading(false);
    }
  }, []);

  const fetchSearchData = useCallback(async (params) => {
    setLoading(true);
    try {
      const cleanedParams = cleanSearchParams(params);
      const response = await api.post('/cs/members/search', cleanedParams, {
        headers: { 'Content-Type': 'application/json' }
      });
      if (Array.isArray(response.data)) {
        setMembers(response.data);
        setTotalMembers(response.data.length > 0 ? response.data[0].total_count : 0);
      } else {
        setMembers([]);
        setTotalMembers(0);
      }
      setSelectedOptions(cleanedParams);
    } catch (error) {
      console.error('데이터를 불러오는데 실패했습니다.', error);
      setError('데이터를 불러오는데 실패했습니다.');
      setOpen(true);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    if (loading) return;

    const skip = (currentPage - 1) * membersPerPage;
    const params = { skip, limit: membersPerPage };

    if (Object.keys(selectedOptions).length > 0) {
      fetchSearchData({ ...selectedOptions, skip, limit: membersPerPage });
    } else {
      fetchActivityData(params);
    }
  }, [currentPage, selectedOptions, fetchActivityData, fetchSearchData]);

  const handleSearch = () => {
    if (loading) return;
    setCurrentPage(1);
    fetchSearchData({
      ...searchParams,
      skip: 0,
      limit: membersPerPage,
    });
  };

  const handleReset = () => {
    if (loading) return;
    setSearchParams({
      nickname: '',
      email: '',
      create_date_from: '',
      create_date_to: '',
      category: []
    });
    setSelectedOptions({});
    setCurrentPage(1);
    fetchActivityData({ skip: 0, limit: membersPerPage });
  };

  const handlePageChange = (event, value) => {
    if (loading) return;
    setCurrentPage(value);
  };

  const handleCloseSnackbar = () => {
    setOpen(false);
  };

  const handleCellClick = (endpoint, memberId, nickname) => {
    if (loading) return;
    navigate(endpoint, {
      state: { member_id: memberId, nickname: nickname },
    });
  };

  // Enter 키로 검색 요청 핸들러
  const handleKeyPress = (event) => {
    if (event.key === 'Enter') {
      handleSearch();
    }
  };

  return (
    <Box>
      <Box sx={{ display: 'flex', gap: 2, mb: 2, alignItems: 'center' }}>
        <TextField
          label="닉네임"
          value={searchParams.nickname}
          onChange={(e) => setSearchParams({ ...searchParams, nickname: e.target.value })}
          onKeyPress={handleKeyPress}
          sx={{ width: '150px' }}
        />
        <TextField
          label="이메일"
          value={searchParams.email}
          onChange={(e) => setSearchParams({ ...searchParams, email: e.target.value })}
          onKeyPress={handleKeyPress}
          sx={{ width: '200px' }}
        />
        <TextField
          label="가입일(시작)"
          type="date"
          InputLabelProps={{ shrink: true }}
          value={searchParams.create_date_from}
          onChange={(e) => setSearchParams({ ...searchParams, create_date_from: e.target.value })}
          sx={{ width: '150px' }}
        />
        <TextField
          label="가입일(끝)"
          type="date"
          InputLabelProps={{ shrink: true }}
          value={searchParams.create_date_to}
          onChange={(e) => setSearchParams({ ...searchParams, create_date_to: e.target.value })}
          sx={{ width: '150px' }}
        />
        <FormControl sx={{ width: '150px' }}>
          <InputLabel>카테고리</InputLabel>
          <Select
            multiple
            value={searchParams.category}
            onChange={(e) => setSearchParams({ ...searchParams, category: e.target.value })}
          >
            <MenuItem value="PLAY">연극</MenuItem>
            <MenuItem value="MOVIE">영화</MenuItem>
            <MenuItem value="PERFORMANCE">공연</MenuItem>
            <MenuItem value="CONCERT">콘서트</MenuItem>
            <MenuItem value="MUSICAL">뮤지컬</MenuItem>
            <MenuItem value="EXHIBITION">전시</MenuItem>
            <MenuItem value="ETC">기타</MenuItem>
          </Select>
        </FormControl>
        <Button variant="contained" onClick={handleSearch} sx={{ width: '100px' }} disabled={loading}>검색</Button>
        <Button variant="outlined" onClick={handleReset} sx={{ width: '100px' }} disabled={loading}>초기화</Button>
      </Box>

      {Object.keys(selectedOptions).length > 0 && (
        <Box sx={{ mb: 2 }}>
          <Typography variant="subtitle1">선택된 검색 옵션:</Typography>
          <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
            {selectedOptions.nickname && <Chip label={`닉네임: ${selectedOptions.nickname}`} />}
            {selectedOptions.email && <Chip label={`이메일: ${selectedOptions.email}`} />}
            {selectedOptions.create_date_from && <Chip label={`가입일(시작): ${selectedOptions.create_date_from}`} />}
            {selectedOptions.create_date_to && <Chip label={`가입일(끝): ${selectedOptions.create_date_to}`} />}
            {selectedOptions.category && selectedOptions.category.length > 0 && (
              <Chip label={`카테고리: ${selectedOptions.category.join(', ')}`} />
            )}
          </Box>
        </Box>
      )}

      {members.length === 0 ? (
        <Typography variant="body1">검색 결과가 없습니다.</Typography>
      ) : (
        <Paper elevation={3}>
          <Table sx={{ minWidth: 650 }}>
            <TableHead>
              <TableRow>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>ID</TableCell>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>닉네임</TableCell>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>이메일</TableCell>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>가입일</TableCell>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>타입</TableCell>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>카테고리</TableCell>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>스케줄 수</TableCell>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>리뷰 수</TableCell>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>댓글 수</TableCell>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>예약 수</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {members.map((member) => (
                <TableRow key={member.member_id} hover>
                  <TableCell onClick={() => navigate(`/members/${member.member_id}`)} style={{ cursor: 'pointer', backgroundColor: '#ffffff' }}>{member.member_id}</TableCell>
                  <TableCell onClick={() => navigate(`/members/${member.member_id}`)} style={{ cursor: 'pointer', backgroundColor: '#ffffff' }}>{member.nickname}</TableCell>
                  <TableCell onClick={() => navigate(`/members/${member.member_id}`)} style={{ cursor: 'pointer', backgroundColor: '#ffffff' }}>{member.email}</TableCell>
                  <TableCell onClick={() => navigate(`/members/${member.member_id}`)} style={{ cursor: 'pointer', backgroundColor: '#ffffff' }}>{new Date(member.create_date).toLocaleDateString('ko-KR')}</TableCell>
                  <TableCell onClick={() => navigate(`/members/${member.member_id}`)} style={{ cursor: 'pointer', backgroundColor: '#ffffff' }}>{member.type}</TableCell>
                  <TableCell style={{ backgroundColor: '#ffffff' }}>{member.categories ? member.categories.join(', ') : 'N/A'}</TableCell>
                  <TableCell
                    onClick={() => handleCellClick('/schedules', member.member_id, member.nickname)}
                    style={{ cursor: 'pointer', backgroundColor: '#ffffff' }}
                  >
                    {member.schedules_count}
                  </TableCell>
                  <TableCell
                    onClick={() => handleCellClick('/reviews', member.member_id, member.nickname)}
                    style={{ cursor: 'pointer', backgroundColor: '#ffffff' }}
                  >
                    {member.reviews_count}
                  </TableCell>
                  <TableCell onClick={() => handleCellClick('/comments', member.member_id, member.nickname)} style={{ cursor: 'pointer', backgroundColor: '#ffffff' }}>{member.comments_count}</TableCell>
                  <TableCell onClick={() => handleCellClick('/reservations', member.member_id, member.nickname)} style={{ cursor: 'pointer', backgroundColor: '#ffffff' }}>{member.reservations_count}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Paper>
      )}

      <Pagination
        count={maxPages}
        page={currentPage || 1}
        onChange={handlePageChange}
        variant="outlined"
        shape="rounded"
        showFirstButton
        showLastButton
        siblingCount={3}
        boundaryCount={2}
        sx={{ mt: 2 }}
      />
      <Snackbar open={open} autoHideDuration={6000} onClose={handleCloseSnackbar}>
        <Alert onClose={handleCloseSnackbar} severity="error" sx={{ width: '100%' }}>
          {error}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default Members;
