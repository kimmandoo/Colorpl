import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import {
  Table, TableBody, TableCell, TableHead, TableRow, Box, Button, TextField,
  FormControl, InputLabel, Select, MenuItem, Pagination, Snackbar, Alert, Typography,
  Chip, Modal
} from '@mui/material';
import api from '../api';

const Comments = () => {
  const [comments, setComments] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const commentsPerPage = 10;
  const maxPages = 100;
  const [error, setError] = useState('');
  const [open, setOpen] = useState(false);
  const [openModal, setOpenModal] = useState(false);
  const [modalMessage, setModalMessage] = useState('');
  const [selectedOptions, setSelectedOptions] = useState({});
  const [initialLoadDone, setInitialLoadDone] = useState(false);

  const [searchParams, setSearchParams] = useState({
    nickname: '',
    content: '',
    create_date_from: '',
    create_date_to: '',
    review_id: '',
    is_inappropriate: '',
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

  // 기본 데이터 로드
  const loadActivityData = useCallback(async () => {
    const skip = (currentPage - 1) * commentsPerPage;
    try {
      const response = await api.get('/cs/comments/activity', {
        params: {
          skip: skip,
          limit: commentsPerPage,
        },
      });

      setComments(response.data);
      if (response.data.length === 0 && currentPage > 1) {
        setOpen(true); // 페이지 범위를 초과했을 때 Snackbar 열기
      }
    } catch (error) {
      console.error('댓글 정보를 불러오는데 실패했습니다.', error);
      setError('댓글 정보를 불러오는데 실패했습니다.');
      setOpen(true);
    }
  }, [currentPage]);

  // 검색 요청 처리
  const loadSearchData = useCallback(
    async (params) => {
      try {
        const cleanedParams = cleanSearchParams(params);
        const response = await api.post('/cs/comments/search', cleanedParams, {
          headers: { 'Content-Type': 'application/json' },
        });
  
        if (Array.isArray(response.data)) {
          setComments(response.data);
          if (response.data.length === 0) {
            setOpen(true); // 데이터가 부족할 경우 Snackbar 열기
          }
        } else {
          setComments([]);
          setOpen(true); // 데이터가 부족할 경우 Snackbar 열기
        }
  
        setSelectedOptions(cleanedParams); // 사용자가 선택한 옵션을 저장
      } catch (error) {
        console.error('데이터를 불러오는데 실패했습니다.', error);
        setError('데이터를 불러오는데 실패했습니다.');
        setOpen(true);
      }
    },
    [],
  );

  // 페이지 변경 또는 초기 로드 시 데이터 로드
  useEffect(() => {
    const fetchInitialData = async () => {
      const skip = (currentPage - 1) * commentsPerPage;
      
      if (location.state && !initialLoadDone) {
        const params = {
          ...location.state,
          skip,
          limit: commentsPerPage,
        };
        await loadSearchData(params);
        setInitialLoadDone(true);
        navigate(location.pathname, { replace: true });
      } else if (Object.keys(selectedOptions).length > 0) {
        const params = {
          ...selectedOptions,
          skip,
          limit: commentsPerPage,
        };
        loadSearchData(params);
      } else {
        loadActivityData();
      }
    };

    fetchInitialData();
  }, [currentPage, location.state, loadSearchData, loadActivityData, initialLoadDone]);

  // 검색 기능을 활성화하는 POST 요청
  const handleSearch = () => {
    setCurrentPage(1);
    const params = {
      ...searchParams,
      skip: 0,
      limit: commentsPerPage,
    };
  
    loadSearchData(params);
  };

  const handleReset = () => {
    setSearchParams({
      nickname: '',
      content: '',
      create_date_from: '',
      create_date_to: '',
      review_id: '',
      is_inappropriate: '',
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
      {/* 검색 영역 */}
      <Box sx={{ display: 'flex', gap: 2, mb: 2, alignItems: 'center' }}>
        {/* 검색 필드들 */}
        <TextField
          label="닉네임"
          value={searchParams.nickname}
          onChange={(e) => setSearchParams({ ...searchParams, nickname: e.target.value })}
          sx={{ width: '150px' }}
        />
        <TextField
          label="내용"
          value={searchParams.content}
          onChange={(e) => setSearchParams({ ...searchParams, content: e.target.value })}
          sx={{ width: '200px' }}
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
        <FormControl sx={{ width: '150px' }}>
          <InputLabel>규정 위반 여부</InputLabel>
          <Select
            value={searchParams.is_inappropriate}
            onChange={(e) => setSearchParams({ ...searchParams, is_inappropriate: e.target.value })}
          >
            <MenuItem value="">전체</MenuItem>
            <MenuItem value="true">규정 위반</MenuItem>
            <MenuItem value="false">정상</MenuItem>
          </Select>
        </FormControl>
        <Button variant="contained" onClick={handleSearch} sx={{ width: '100px' }}>
          검색
        </Button>
        <Button variant="outlined" onClick={handleReset} sx={{ width: '100px' }}>
          초기화
        </Button>
      </Box>
  
      {/* 선택된 검색 옵션을 표시하는 영역 */}
      {Object.keys(selectedOptions).length > 0 && (
        <Box sx={{ mb: 2 }}>
          <Typography variant="subtitle1">선택된 검색 옵션:</Typography>
          <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
            {selectedOptions.nickname && (
              <Chip label={`닉네임: ${selectedOptions.nickname}`} />
            )}
            {selectedOptions.content && (
              <Chip label={`내용: ${selectedOptions.content}`} />
            )}
            {selectedOptions.create_date_from && (
              <Chip label={`생성일(시작): ${selectedOptions.create_date_from}`} />
            )}
            {selectedOptions.create_date_to && (
              <Chip label={`생성일(끝): ${selectedOptions.create_date_to}`} />
            )}
            {selectedOptions.is_inappropriate !== undefined && (
              <Chip
                label={`규정 위반 여부: ${
                  selectedOptions.is_inappropriate === 'true' ? '위반' : '정상'
                }`}
              />
            )}
          </Box>
        </Box>
      )}
  
      {/* 데이터가 로드되지 않았거나 비어 있을 때 메시지 표시 */}
      {comments.length === 0 ? (
        <Typography variant="body1">검색 결과가 없습니다.</Typography>
      ) : (
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>닉네임</TableCell>
              <TableCell>생성일</TableCell>
              <TableCell>내용</TableCell>
              <TableCell>규정 위반 여부</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {comments.map((comment) => (
              <TableRow 
                key={comment.comment_id} 
                hover 
                onClick={() => navigate(`/comments/${comment.comment_id}`)}
                style={{ cursor: 'pointer' }}
              >
                <TableCell>{comment.comment_id}</TableCell>
                <TableCell 
                  onClick={(e) => {
                    e.stopPropagation(); // 행 클릭 이벤트가 실행되지 않도록 막음
                    navigate(`/members/${comment.member_id}`);
                  }} 
                  style={{ cursor: 'pointer' }}
                >
                  {comment.nickname}
                </TableCell>
                <TableCell>{new Date(comment.create_date).toLocaleDateString('ko-KR')}</TableCell>
                <TableCell>{comment.content}</TableCell>
                <TableCell>{comment.is_inappropriate ? '위반' : '정상'}</TableCell>
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

export default Comments;
