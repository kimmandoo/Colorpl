import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Table, TableBody, TableCell, TableHead, TableRow, Box, Button, TextField, FormControl, InputLabel, Select, MenuItem, Pagination, Snackbar, Alert, Typography, Chip, Checkbox, FormControlLabel, Modal } from '@mui/material';
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
    nickname: '',
    email: '',
    schedule_name: '',
    schedule_category: '',
    review_written: false,
    is_reserved: false,
  });

  const navigate = useNavigate();
  const location = useLocation();

  // 빈 값 제거 함수
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
      
      if (Array.isArray(response.data)) {
        setSchedules(response.data);
        if (response.data.length === 0) {
          setOpen(true);
        }
      } else {
        setSchedules([]);
        setOpen(true);
      }

      setSelectedOptions(cleanedParams);
    } catch (error) {
      console.error('데이터를 불러오는데 실패했습니다.', error);
      setError('데이터를 불러오는데 실패했습니다.');
      setOpen(true);
    }
  }, []);

  useEffect(() => {
    const fetchInitialData = async () => {
      const skip = (currentPage - 1) * schedulesPerPage;

      if (location.state) {
        const { member_id, nickname } = location.state;
        const params = { member_id, nickname, skip, limit: schedulesPerPage };
        loadData(params);
      } else {
        try {
          const response = await api.get('/cs/schedules', {
            params: {
              skip,
              limit: schedulesPerPage,
            },
          });

          if (response.data.length === 0 && currentPage > 1) {
            setOpen(true);
          } else {
            setSchedules(Array.isArray(response.data) ? response.data : []);
          }
        } catch (error) {
          console.error('스케줄 정보를 불러오는데 실패했습니다.', error);
          setError('스케줄 정보를 불러오는데 실패했습니다.');
          setOpen(true);
        }
      }
    };

    fetchInitialData();
  }, [currentPage, location.state, loadData]);

  const handleSearch = () => {
    setCurrentPage(1);

    const params = {
      ...searchParams,
      skip: 0,
      limit: schedulesPerPage,
    };

    loadData(params);
  };

  const handleReset = () => {
    setSearchParams({
      nickname: '',
      email: '',
      schedule_name: '',
      schedule_category: '',
      review_written: false,
      is_reserved: false,
    });
    setSelectedOptions({});
    setCurrentPage(1);
    loadData({ skip: 0, limit: schedulesPerPage });

    navigate(location.pathname, { replace: true }); // URL에서 state 제거
  };

  const handlePageChange = (event, value) => {
    setCurrentPage(value);
  };

  const handleCloseSnackbar = () => {
    setOpen(false); 
  };

  const handleCloseModal = () => {
    setOpenModal(false);
  };

  return (
    <Box>
      <Box sx={{ display: 'flex', gap: 2, mb: 2, alignItems: 'center' }}>
        <TextField
          label="닉네임"
          value={searchParams.nickname}
          onChange={(e) => setSearchParams({ ...searchParams, nickname: e.target.value })}
          sx={{ width: '150px' }}
        />
        <TextField
          label="이메일"
          value={searchParams.email}
          onChange={(e) => setSearchParams({ ...searchParams, email: e.target.value })}
          sx={{ width: '200px' }}
        />
        <TextField
          label="스케줄명"
          value={searchParams.schedule_name}
          onChange={(e) => setSearchParams({ ...searchParams, schedule_name: e.target.value })}
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
        <FormControlLabel
          control={
            <Checkbox
              checked={searchParams.is_reserved}
              onChange={(e) => setSearchParams({ ...searchParams, is_reserved: e.target.checked })}
            />
          }
          label="예매 여부"
        />
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
            {selectedOptions.is_reserved && (
              <Chip label={`예매 여부: ${selectedOptions.is_reserved ? 'Yes' : 'No'}`} />
            )}
          </Box>
        </Box>
      )}

      {schedules.length === 0 ? (
        <Typography variant="body1">검색 결과가 없습니다.</Typography>
      ) : (
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>닉네임</TableCell>
              <TableCell>이메일</TableCell>
              <TableCell>등록 시간</TableCell>
              <TableCell>스케줄명</TableCell>
              <TableCell>카테고리</TableCell>
              <TableCell>리뷰 작성 여부</TableCell>
              <TableCell>예매 여부</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {schedules.map((schedule) => (
              <TableRow key={schedule.schedule_id} hover style={{ cursor: 'pointer' }}>
                <TableCell onClick={() => navigate(`/schedules/${schedule.schedule_id}`)}>{schedule.schedule_id}</TableCell>
                <TableCell onClick={() => navigate(`/members/${schedule.member_id}`)}>{schedule.nickname}</TableCell>
                <TableCell onClick={() => navigate(`/members/${schedule.member_id}`)}>{schedule.email}</TableCell>
                <TableCell onClick={() => navigate(`/schedules/${schedule.schedule_id}`)}>{new Date(schedule.schedule_date_time).toLocaleString('ko-KR')}</TableCell>
                <TableCell onClick={() => navigate(`/schedules/${schedule.schedule_id}`)}>
                  {schedule.schedule_name.length > 20 ? `${schedule.schedule_name.substring(0, 20)}...` : schedule.schedule_name}
                </TableCell>
                <TableCell onClick={() => navigate(`/schedules/${schedule.schedule_id}`)}>{schedule.schedule_category}</TableCell>
                <TableCell onClick={() => {
                  if (schedule.review_written && schedule.review_id) {
                    navigate(`/reviews/${schedule.review_id}`);
                  } else {
                    setModalMessage('등록된 리뷰가 없습니다');
                    setOpenModal(true);
                  }
                }}>{schedule.review_written ? 'Yes' : 'No'}</TableCell>
                <TableCell onClick={() => {
                  if (schedule.is_reserved) {
                    navigate(`/reservations/${schedule.reservation_id}`);
                  } else {
                    setModalMessage('등록된 예매가 없습니다');
                    setOpenModal(true);
                  }
                }}>{schedule.is_reserved ? 'Yes' : 'No'}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
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
