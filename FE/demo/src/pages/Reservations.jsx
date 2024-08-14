import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import {
  Table, TableBody, TableCell, TableHead, TableRow, Box, Button, TextField,
  FormControl, InputLabel, Select, MenuItem, Pagination, Snackbar, Alert, Typography,
  Chip, Modal, Paper, TableContainer
} from '@mui/material';
import api from '../api';

const ReservationPage = () => {
  const [reservations, setReservations] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const reservationsPerPage = 10;
  const maxPages = 100;
  const [totalReservations, setTotalReservations] = useState(0);
  const [error, setError] = useState('');
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [openModal, setOpenModal] = useState(false);
  const [modalMessage, setModalMessage] = useState('');
  const [selectedOptions, setSelectedOptions] = useState({});
  const [initialLoadDone, setInitialLoadDone] = useState(false);

  const [searchParams, setSearchParams] = useState({
    nickname: '',
    email: '',
    reserve_id: '',
    show_detail_category: '',
    is_refunded: '',
  });

  const navigate = useNavigate();
  const location = useLocation();

  const cleanSearchParams = (params) => {
    const cleanedParams = { ...params };
    Object.keys(cleanedParams).forEach((key) => {
      if (!cleanedParams[key]) {
        delete cleanedParams[key];
      }
    });
    return cleanedParams;
  };

  const loadActivityData = useCallback(async () => {
    const skip = (currentPage - 1) * reservationsPerPage;
    try {
      const response = await api.get('/vm/reservations/activity', {
        params: {
          skip: skip,
          limit: reservationsPerPage,
        },
      });

      setReservations(response.data);
      setTotalReservations(response.data.length > 0 ? response.data[0].total_count : 0);
      if (response.data.length === 0 && currentPage > 1) {
        setOpenSnackbar(true);
      }
    } catch (error) {
      console.error('예약 정보를 불러오는데 실패했습니다.', error);
      setError('예약 정보를 불러오는데 실패했습니다.');
      setOpenSnackbar(true);
    }
  }, [currentPage]);

  const loadSearchData = useCallback(
    async (params) => {
      try {
        const cleanedParams = cleanSearchParams(params);
        const response = await api.post('/vm/reservations/search', cleanedParams, {
          headers: { 'Content-Type': 'application/json' },
        });

        setReservations(response.data);
        setTotalReservations(response.data.length > 0 ? response.data[0].total_count : 0);
        if (response.data.length === 0) {
          setOpenSnackbar(true);
        }

        setSelectedOptions(cleanedParams);
      } catch (error) {
        console.error('데이터를 불러오는데 실패했습니다.', error);
        setError('데이터를 불러오는데 실패했습니다.');
        setOpenSnackbar(true);
      }
    },
    [],
  );

  useEffect(() => {
    const fetchInitialData = async () => {
      const skip = (currentPage - 1) * reservationsPerPage;

      if (location.state && !initialLoadDone) {
        const params = {
          ...location.state,
          skip,
          limit: reservationsPerPage,
        };
        await loadSearchData(params);
        setInitialLoadDone(true);
        navigate(location.pathname, { replace: true });
      } else if (Object.keys(selectedOptions).length > 0) {
        const params = {
          ...selectedOptions,
          skip,
          limit: reservationsPerPage,
        };
        loadSearchData(params);
      } else {
        loadActivityData();
      }
    };

    fetchInitialData();
  }, [currentPage, location.state, loadSearchData, loadActivityData, initialLoadDone]);

  const handleSearch = () => {
    setCurrentPage(1);
    const params = {
      ...searchParams,
      skip: 0,
      limit: reservationsPerPage,
    };

    loadSearchData(params);
  };

  const handleReset = () => {
    setSearchParams({
      nickname: '',
      email: '',
      reserve_id: '',
      show_detail_category: '',
      is_refunded: '',
    });
    setSelectedOptions({});
    setCurrentPage(1);
    loadActivityData();
  };

  const handlePageChange = (event, value) => {
    setCurrentPage(value);
  };

  const handleCloseSnackbar = () => {
    setOpenSnackbar(false);
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
          label="예약 ID"
          value={searchParams.reserve_id}
          onChange={(e) => setSearchParams({ ...searchParams, reserve_id: e.target.value })}
          sx={{ width: '150px' }}
        />
        <FormControl sx={{ width: '150px' }}>
          <InputLabel>카테고리</InputLabel>
          <Select
            value={searchParams.show_detail_category}
            onChange={(e) => setSearchParams({ ...searchParams, show_detail_category: e.target.value })}
          >
            <MenuItem value="">전체</MenuItem>
            <MenuItem value="PLAY">연극</MenuItem>
            <MenuItem value="MOVIE">영화</MenuItem>
            <MenuItem value="PERFORMANCE">공연</MenuItem>
            <MenuItem value="CONCERT">콘서트</MenuItem>
            <MenuItem value="MUSICAL">뮤지컬</MenuItem>
            <MenuItem value="EXHIBITION">전시</MenuItem>
            <MenuItem value="ETC">기타</MenuItem>
          </Select>
        </FormControl>
        <FormControl sx={{ width: '150px' }}>
          <InputLabel>환불 여부</InputLabel>
          <Select
            value={searchParams.is_refunded}
            onChange={(e) => setSearchParams({ ...searchParams, is_refunded: e.target.value })}
          >
            <MenuItem value="">전체</MenuItem>
            <MenuItem value="true">환불</MenuItem>
            <MenuItem value="false">미환불</MenuItem>
          </Select>
        </FormControl>
        <Button variant="contained" onClick={handleSearch} sx={{ width: '100px' }}>
          검색
        </Button>
        <Button variant="outlined" onClick={handleReset} sx={{ width: '100px' }}>
          초기화
        </Button>
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
            {selectedOptions.reserve_id && (
              <Chip label={`예약 ID: ${selectedOptions.reserve_id}`} />
            )}
            {selectedOptions.show_detail_category && (
              <Chip label={`카테고리: ${selectedOptions.show_detail_category}`} />
            )}
            {selectedOptions.is_refunded !== undefined && (
              <Chip
                label={`환불 여부: ${
                  selectedOptions.is_refunded === 'true' ? '환불' : '미환불'
                }`}
              />
            )}
          </Box>
        </Box>
      )}

      {reservations.length === 0 ? (
        <Typography variant="body1">검색 결과가 없습니다.</Typography>
      ) : (
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>ID</TableCell>
                <TableCell>닉네임</TableCell>
                <TableCell>생성일</TableCell>
                <TableCell>예약 금액</TableCell>
                <TableCell>예약 날짜</TableCell>
                <TableCell>카테고리</TableCell>
                <TableCell>환불 여부</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {reservations.map((reservation, index) => (
                <TableRow 
                  key={`${reservation.reserve_id}-${index}`}  // 고유한 키로 수정
                  hover 
                  onClick={() => navigate(`/reservations/${reservation.reserve_id}`)}
                  style={{ cursor: 'pointer' }}
                >
                  <TableCell>{reservation.reserve_id}</TableCell>
                  <TableCell 
                    onClick={(e) => {
                      e.stopPropagation();
                      navigate(`/members/${reservation.member_id}`);
                    }} 
                    style={{ cursor: 'pointer' }}
                  >
                    {reservation.nickname}
                  </TableCell>
                  <TableCell>{new Date(reservation.create_date).toLocaleDateString('ko-KR')}</TableCell>
                  <TableCell>{reservation.reserve_amount}</TableCell>
                  <TableCell>{new Date(reservation.reserve_date).toLocaleDateString('ko-KR')}</TableCell>
                  <TableCell>{reservation.show_detail_category}</TableCell>
                  <TableCell>{reservation.is_refunded ? '환불' : '미환불'}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
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
      <Snackbar open={openSnackbar} autoHideDuration={6000} onClose={handleCloseSnackbar}>
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

export default ReservationPage;
