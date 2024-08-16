import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Table, TableBody, TableCell, TableHead, TableRow, Box, Button, TextField, FormControl, InputLabel, Select, MenuItem, Pagination, Snackbar, Alert, Typography, Chip, Checkbox, FormControlLabel, Modal, Paper } from '@mui/material';
import api from '../api';

const Schedules = () => {
  const [schedules, setSchedules] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const schedulesPerPage = 10;
  const maxPages = 100;
  const [error, setError] = useState('');
  const [open, setOpen] = useState(false);
  const [openModal, setOpenModal] = useState(false);
  const [modalMessage, setModalMessage] = useState('');
  const [selectedOptions, setSelectedOptions] = useState({});

  const [searchParams, setSearchParams] = useState({
    member_id: '',
    nickname: '',
    email: '',
    schedule_name: '',
    schedule_category: '',
    review_written: false,
    dtype: '',
  });

  const navigate = useNavigate();
  const location = useLocation();

  // 초기 로딩 시 멤버 ID와 닉네임을 검색 조건에 반영
  useEffect(() => {
    if (location.state) {
      const { member_id, nickname } = location.state;
      setSearchParams((prevParams) => ({
        ...prevParams,
        member_id: member_id || prevParams.member_id,
        nickname: nickname || prevParams.nickname,
      }));
    }
  }, [location.state]);

  const cleanSearchParams = (params) => {
    const cleanedParams = { ...params };
    Object.keys(cleanedParams).forEach((key) => {
      if (cleanedParams[key] === '' || cleanedParams[key] === null || cleanedParams[key] === undefined) {
        delete cleanedParams[key];
      }
    });
    return cleanedParams;
  };

  const loadData = useCallback(async (params) => {
    try {
      const cleanedParams = cleanSearchParams(params);
      const response = await api.post('/cs/schedules/search', cleanedParams, {
        headers: { 'Content-Type': 'application/json' }
      });

      setSchedules(Array.isArray(response.data) ? response.data : []);

      if (Array.isArray(response.data) && response.data.length === 0) {
        setOpen(true);
      }

      setSelectedOptions(cleanedParams);
    } catch (error) {
      console.error('데이터를 불러오는데 실패했습니다.', error);
      setError('데이터를 불러오는데 실패했습니다.');
      setSchedules([]);
      setOpen(true);
    }
  }, []);

  // 초기 로딩 및 페이지 변경 시 데이터를 로드
  useEffect(() => {
    const skip = (currentPage - 1) * schedulesPerPage;
    const params = { ...searchParams, skip, limit: schedulesPerPage };

    // 검색 조건이 있을 경우 POST, 없을 경우 GET
    if (Object.keys(cleanSearchParams(searchParams)).length > 0) {
      loadData(params);
    } else {
      fetchInitialData();
    }
  }, [currentPage, searchParams, loadData]);

  // 기본 스케줄 로딩
  const fetchInitialData = async () => {
    try {
      const response = await api.get('/cs/schedules', {
        params: {
          skip: (currentPage - 1) * schedulesPerPage,
          limit: schedulesPerPage,
        },
      });
      setSchedules(Array.isArray(response.data) ? response.data : []);
      if (response.data.length === 0) {
        setOpen(true);
      }
    } catch (error) {
      console.error('스케줄 정보를 불러오는데 실패했습니다.', error);
      setError('스케줄 정보를 불러오는데 실패했습니다.');
      setSchedules([]);
      setOpen(true);
    }
  };

  const handleSearch = () => {
    setCurrentPage(1);
    const params = { ...searchParams, skip: 0, limit: schedulesPerPage };
    loadData(params);
  };

  const handleReset = () => {
    setSearchParams({
        member_id: '',
        nickname: '',
        email: '',
        schedule_name: '',
        schedule_category: '',
        review_written: false,
        dtype: '',
    });
    setSelectedOptions({});
    setCurrentPage(1);

    // API 경로가 아닌 페이지 경로로 navigate
    navigate('/schedules'); // 올바른 페이지 경로로 이동
};

  const handlePageChange = (event, value) => {
    setCurrentPage(value);
  };

  const handleCloseSnackbar = () => setOpen(false);
  const handleCloseModal = () => setOpenModal(false);
  const handleKeyPress = (event) => {
    if (event.key === 'Enter') handleSearch();
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
          label="스케줄명"
          value={searchParams.schedule_name}
          onChange={(e) => setSearchParams({ ...searchParams, schedule_name: e.target.value })}
          onKeyPress={handleKeyPress}
          sx={{ width: '150px' }}
        />
        <FormControl sx={{ width: '150px' }}>
          <InputLabel>카테고리</InputLabel>
          <Select
            value={searchParams.schedule_category}
            onChange={(e) => setSearchParams({ ...searchParams, schedule_category: e.target.value })}
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
        <FormControlLabel
          control={
            <Checkbox
              checked={searchParams.review_written}
              onChange={(e) => setSearchParams({ ...searchParams, review_written: e.target.checked })}
            />
          }
          label="리뷰 작성 여부"
        />
        <FormControl sx={{ width: '150px' }}>
          <InputLabel>타입</InputLabel>
          <Select
            value={searchParams.dtype}
            onChange={(e) => setSearchParams({ ...searchParams, dtype: e.target.value })}
          >
            <MenuItem value="C">Custom</MenuItem>
            <MenuItem value="R">Reservation</MenuItem>
          </Select>
        </FormControl>
        <Button variant="contained" onClick={handleSearch} sx={{ width: '100px' }}>검색</Button>
        <Button variant="outlined" onClick={handleReset} sx={{ width: '100px' }}>초기화</Button>
      </Box>
      
      {Object.keys(selectedOptions).length > 0 && (
        <Box sx={{ mb: 2 }}>
          <Typography variant="subtitle1">선택된 검색 옵션:</Typography>
          <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
            {selectedOptions.nickname && (
              <Chip label={`닉네임: ${selectedOptions.nickname}`} />
            )}
            {selectedOptions.email && (
              <Chip label={`이메일: ${selectedOptions.email}`} />
            )}
            {selectedOptions.schedule_name && (
              <Chip label={`스케줄명: ${selectedOptions.schedule_name}`} />
            )}
            {selectedOptions.schedule_category && (
              <Chip label={`카테고리: ${selectedOptions.schedule_category}`} />
            )}
            {selectedOptions.review_written && (
              <Chip label={`리뷰 작성 여부: ${selectedOptions.review_written ? 'Yes' : 'No'}`} />
            )}
            {selectedOptions.dtype && (
              <Chip label={`타입: ${selectedOptions.dtype === 'C' ? 'Custom' : 'Reservation'}`} />
            )}
          </Box>
        </Box>
      )}

      {schedules.length === 0 ? (
        <Typography variant="body1">검색 결과가 없습니다.</Typography>
      ) : (
        <Paper elevation={3}>
          <Table sx={{ minWidth: 650 }}>
            <TableHead>
              <TableRow>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>ID</TableCell>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>닉네임</TableCell>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>이메일</TableCell>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>등록 시간</TableCell>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>스케줄명</TableCell>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>카테고리</TableCell>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>리뷰 작성 여부</TableCell>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>타입</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {schedules.map((schedule, index) => (
                <TableRow key={`${schedule.schedule_id}-${index}`} hover style={{ cursor: 'pointer' }}>
                  <TableCell onClick={() => navigate(`/schedules/${schedule.schedule_id}`)} style={{ backgroundColor: '#ffffff' }}>{schedule.schedule_id}</TableCell>
                  <TableCell onClick={() => navigate(`/members/${schedule.member_id}`)} style={{ backgroundColor: '#ffffff' }}>{schedule.nickname}</TableCell>
                  <TableCell onClick={() => navigate(`/members/${schedule.member_id}`)} style={{ backgroundColor: '#ffffff' }}>{schedule.email}</TableCell>
                  <TableCell onClick={() => navigate(`/schedules/${schedule.schedule_id}`)} style={{ backgroundColor: '#ffffff' }}>{new Date(schedule.schedule_date_time).toLocaleString('ko-KR')}</TableCell>
                  <TableCell onClick={() => navigate(`/schedules/${schedule.schedule_id}`)} style={{ backgroundColor: '#ffffff' }}>
                    {schedule.schedule_name && schedule.schedule_name.length > 20 ? `${schedule.schedule_name.substring(0, 20)}...` : schedule.schedule_name}
                  </TableCell>
                  <TableCell onClick={() => navigate(`/schedules/${schedule.schedule_id}`)} style={{ backgroundColor: '#ffffff' }}>{schedule.schedule_category}</TableCell>
                  <TableCell onClick={() => {
                    if (schedule.review_written && schedule.review_id) {
                      navigate(`/reviews/${schedule.review_id}`);
                    } else {
                      setModalMessage('등록된 리뷰가 없습니다');
                      setOpenModal(true);
                    }
                  }} style={{ backgroundColor: '#ffffff' }}>{schedule.review_written ? 'Yes' : 'No'}</TableCell>
                  <TableCell style={{ backgroundColor: '#ffffff' }}>{schedule.dtype === 'R' ? 'Reservation' : 'Custom'}</TableCell>
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

      <Modal open={openModal} onClose={handleCloseModal}>
        <Box sx={{ position: 'absolute', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', bgcolor: 'background.paper', p: 4 }}>
          <Typography>{modalMessage}</Typography>
          <Button onClick={handleCloseModal} sx={{ mt: 2 }}>닫기</Button>
        </Box>
      </Modal>
    </Box>
  );
};

export default Schedules;
