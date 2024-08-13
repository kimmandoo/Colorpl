import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Table, TableBody, TableCell, TableHead, TableRow, Box, Button, TextField, FormControl, InputLabel, Select, MenuItem, Pagination, Snackbar, Alert, Typography, Chip, Modal } from '@mui/material';
import api from '../api';

const Reviews = () => {
  const [reviews, setReviews] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const reviewsPerPage = 10;
  const maxPages = 100;
  const [error, setError] = useState('');
  const [open, setOpen] = useState(false);
  const [openModal, setOpenModal] = useState(false);
  const [modalMessage, setModalMessage] = useState('');
  const [selectedOptions, setSelectedOptions] = useState({});
  const [initialLoadDone, setInitialLoadDone] = useState(false);

  const [searchParams, setSearchParams] = useState({
    nickname: '',
    create_date_from: '',
    create_date_to: '',
    schedule_name: '',
    is_spoiler: '',
    schedule_category: ''
  });

  const navigate = useNavigate();
  const location = useLocation();

  const cleanSearchParams = (params) => {
    const cleanedParams = { ...params };
    Object.keys(cleanedParams).forEach((key) => {
      if (cleanedParams[key] === '' || cleanedParams[key] === null || cleanedParams[key] === undefined) {
        delete cleanedParams[key];
      }
    });
    return cleanedParams;
  };

  const loadActivityData = useCallback(async () => {
    const skip = (currentPage - 1) * reviewsPerPage;
    try {
      const response = await api.get('/cs/reviews/activity', {
        params: {
          skip: skip,
          limit: reviewsPerPage,
        },
      });

      setReviews(response.data);
      if (response.data.length === 0 && currentPage > 1) {
        setOpen(true);
      }
    } catch (error) {
      console.error('리뷰 정보를 불러오는데 실패했습니다.', error);
      setError('리뷰 정보를 불러오는데 실패했습니다.');
      setOpen(true);
    }
  }, [currentPage]);

  const loadSearchData = useCallback(async (params) => {
    try {
      const cleanedParams = cleanSearchParams(params);
      const response = await api.post('/cs/reviews/search', cleanedParams, {
        headers: { 'Content-Type': 'application/json' }
      });

      if (Array.isArray(response.data)) {
        setReviews(response.data);
        if (response.data.length === 0) {
          setOpen(true);
        }
      } else {
        setReviews([]);
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
      const skip = (currentPage - 1) * reviewsPerPage;
      if (location.state && !initialLoadDone) {
        const { member_id, nickname } = location.state;
        const params = {
          member_id,
          nickname,
          skip,
          limit: reviewsPerPage,
        };
        await loadSearchData(params);
        setInitialLoadDone(true);
        navigate(location.pathname, { replace: true });
      } else if (Object.keys(selectedOptions).length > 0) {
        const params = {
          ...selectedOptions,
          skip,
          limit: reviewsPerPage,
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
      limit: reviewsPerPage,
    };

    loadSearchData(params);
  };

  const handleReset = () => {
    setSearchParams({
      nickname: '',
      create_date_from: '',
      create_date_to: '',
      schedule_name: '',
      is_spoiler: '',
      schedule_category: ''
    });
    setSelectedOptions({});
    setCurrentPage(1);
    loadActivityData();
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
          label="생성일(시작)"
          type="date"
          InputLabelProps={{ shrink: true }}
          value={searchParams.create_date_from}
          onChange={(e) => setSearchParams({ ...searchParams, create_date_from: e.target.value })}
          sx={{ width: '150px' }}
        />
        <TextField
          label="생성일(끝)"
          type="date"
          InputLabelProps={{ shrink: true }}
          value={searchParams.create_date_to}
          onChange={(e) => setSearchParams({ ...searchParams, create_date_to: e.target.value })}
          sx={{ width: '150px' }}
        />
        <TextField
          label="스케줄 이름"
          value={searchParams.schedule_name}
          onChange={(e) => setSearchParams({ ...searchParams, schedule_name: e.target.value })}
          sx={{ width: '200px' }}
        />
        <FormControl sx={{ width: '150px' }}>
          <InputLabel>스포일러 여부</InputLabel>
          <Select
            value={searchParams.is_spoiler}
            onChange={(e) => setSearchParams({ ...searchParams, is_spoiler: e.target.value })}
          >
            <MenuItem value="">전체</MenuItem>
            <MenuItem value="true">스포일러 있음</MenuItem>
            <MenuItem value="false">스포일러 없음</MenuItem>
          </Select>
        </FormControl>
        <FormControl sx={{ width: '150px' }}>
          <InputLabel>카테고리</InputLabel>
          <Select
            value={searchParams.schedule_category}
            onChange={(e) => setSearchParams({ ...searchParams, schedule_category: e.target.value })}
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
            {selectedOptions.create_date_from && (
              <Chip label={`생성일(시작): ${selectedOptions.create_date_from}`} />
            )}
            {selectedOptions.create_date_to && (
              <Chip label={`생성일(끝): ${selectedOptions.create_date_to}`} />
            )}
            {selectedOptions.schedule_name && (
              <Chip label={`스케줄 이름: ${selectedOptions.schedule_name}`} />
            )}
            {selectedOptions.is_spoiler !== undefined && (
              <Chip label={`스포일러 여부: ${selectedOptions.is_spoiler === 'true' ? '있음' : '없음'}`} />
            )}
            {selectedOptions.schedule_category && (
              <Chip label={`카테고리: ${selectedOptions.schedule_category}`} />
            )}
          </Box>
        </Box>
      )}

      {reviews.length === 0 ? (
        <Typography variant="body1">검색 결과가 없습니다.</Typography>
      ) : (
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>닉네임</TableCell>
              <TableCell>생성일</TableCell>
              <TableCell>스케줄 이름</TableCell>
              <TableCell>스포일러 여부</TableCell>
              <TableCell>댓글 수</TableCell>
              <TableCell>카테고리</TableCell>
              <TableCell>리뷰 감정</TableCell>
              <TableCell>공감 수</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {reviews.map((review) => (
              <TableRow key={review.review_id} hover>
                <TableCell onClick={() => navigate(`/reviews/${review.review_id}`)} style={{ cursor: 'pointer' }}>{review.review_id}</TableCell>
                <TableCell onClick={() => navigate(`/members/${review.member_id}`)} style={{ cursor: 'pointer' }}>{review.nickname}</TableCell>
                <TableCell onClick={() => navigate(`/reviews/${review.review_id}`)} style={{ cursor: 'pointer' }}>{new Date(review.create_date).toLocaleDateString('ko-KR')}</TableCell>
                <TableCell onClick={() => navigate(`/reviews/${review.review_id}`)} style={{ cursor: 'pointer' }}>{review.schedule_name}</TableCell>
                <TableCell onClick={() => navigate(`/reviews/${review.review_id}`)} style={{ cursor: 'pointer' }}>{review.is_spoiler ? '있음' : '없음'}</TableCell>
                <TableCell onClick={() => navigate('/comments', { state: { review_id: review.review_id } })} style={{ cursor: 'pointer' }}>{review.comments_count}</TableCell>
                <TableCell onClick={() => navigate(`/reviews/${review.review_id}`)} style={{ cursor: 'pointer' }}>{review.schedule_category}</TableCell>
                <TableCell onClick={() => navigate(`/reviews/${review.review_id}`)} style={{ cursor: 'pointer' }}>{review.review_emotion}</TableCell>
                <TableCell onClick={() => navigate(`/reviews/${review.review_id}`)} style={{ cursor: 'pointer' }}>{review.empathy_num}</TableCell>
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

export default Reviews;
