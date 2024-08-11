import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { Table, TableBody, TableCell, TableHead, TableRow, Box, Button, TextField, FormControl, InputLabel, Select, MenuItem, Pagination, Snackbar, Alert, Typography, Chip, Checkbox, FormControlLabel } from '@mui/material';
import api from '../api';

const Schedules = () => {
  const [schedules, setSchedules] = useState([]);
  const [totalSchedules, setTotalSchedules] = useState(0); // 총 스케줄 수
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지
  const schedulesPerPage = 10; // 페이지당 스케줄 수
  const [error, setError] = useState(''); // 에러 메시지 상태
  const [open, setOpen] = useState(false); // Snackbar 열기 상태
  const [selectedOptions, setSelectedOptions] = useState({}); // 선택된 검색 옵션

  const [searchParams, setSearchParams] = useState({
    nickname: '',
    email: '',
    schedule_name: '',
    schedule_category: '',
    review_written: false,
    is_reserved: false,
  });

  const navigate = useNavigate();

  // 데이터 로드 요청 (검색 또는 기본 데이터 로드)
  const loadData = useCallback(async (params) => {
    try {
      const response = await api.post('/cs/schedules/search', params, {
        headers: { 'Content-Type': 'application/json' }
      });
      console.log('Search results:', response.data); // 응답 데이터 로그

      if (Array.isArray(response.data)) {
        setSchedules(response.data);
        setTotalSchedules(response.data.length);
      } else {
        console.error('Unexpected response structure:', response.data);
        setSchedules([]);
        setTotalSchedules(0);
      }

      setSelectedOptions(params); // 사용자가 선택한 옵션을 저장
    } catch (error) {
      console.error('데이터를 불러오는데 실패했습니다.', error);
      setError('데이터를 불러오는데 실패했습니다.');
      setOpen(true);
    }
  }, []);

  useEffect(() => {
    // 페이지 로드 시 기본 데이터를 GET 요청으로 가져옴
    const fetchInitialData = async () => {
      try {
        const response = await api.get('/cs/schedules', {
          params: {
            skip: (currentPage - 1) * schedulesPerPage,
            limit: schedulesPerPage,
          },
        });
  
        console.log('Fetched schedules:', response.data); // 응답 데이터 로그
        setSchedules(Array.isArray(response.data) ? response.data : []);
        setTotalSchedules(response.data.length || 0);
      } catch (error) {
        console.error('스케줄 정보를 불러오는데 실패했습니다.', error);
        setError('스케줄 정보를 불러오는데 실패했습니다.');
        setOpen(true);
      }
    };
  
    fetchInitialData();
  }, [currentPage]);

  const handleSearch = () => {
    const params = {
      nickname: searchParams.nickname || undefined,
      email: searchParams.email || undefined,
      schedule_name: searchParams.schedule_name || undefined,
      schedule_category: searchParams.schedule_category || undefined,
      review_written: searchParams.review_written || undefined,
      is_reserved: searchParams.is_reserved || undefined,
      skip: (currentPage - 1) * schedulesPerPage,
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
  };

  const handlePageChange = (event, value) => {
    setCurrentPage(value);
  };

  const handleCloseSnackbar = () => {
    setOpen(false); 
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
      
      {/* 선택된 검색 옵션을 표시하는 영역 */}
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
              <TableRow key={schedule.schedule_id} hover onClick={() => navigate(`/schedules/${schedule.schedule_id}`)} style={{ cursor: 'pointer' }}>
                <TableCell>{schedule.schedule_id}</TableCell>
                <TableCell>{schedule.nickname}</TableCell>
                <TableCell>{schedule.email}</TableCell>
                <TableCell>{new Date(schedule.schedule_date_time).toLocaleString('ko-KR')}</TableCell>
                <TableCell>
                  {schedule.schedule_name.length > 20 ? `${schedule.schedule_name.substring(0, 20)}...` : schedule.schedule_name}
                </TableCell>
                <TableCell>{schedule.schedule_category}</TableCell>
                <TableCell>{schedule.review_written ? 'Yes' : 'No'}</TableCell>
                <TableCell>{schedule.is_reserved ? 'Yes' : 'No'}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      )}

      <Pagination
        count={Math.ceil(totalSchedules / schedulesPerPage)}
        page={currentPage}
        onChange={handlePageChange}
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

export default Schedules;
