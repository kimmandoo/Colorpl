import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { Table, TableBody, TableCell, TableHead, TableRow, Box, Button, TextField, FormControl, InputLabel, Select, MenuItem, Pagination, Snackbar, Alert, Typography, Chip } from '@mui/material';
import api from '../api';

const Members = () => {
  const [members, setMembers] = useState([]);
  const [totalMembers, setTotalMembers] = useState(0); // 총 회원 수
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지
  const membersPerPage = 10; // 페이지당 회원 수
  const [error, setError] = useState(''); // 에러 메시지 상태
  const [open, setOpen] = useState(false); // Snackbar 열기 상태
  const [selectedOptions, setSelectedOptions] = useState({}); // 선택된 검색 옵션

  const [searchParams, setSearchParams] = useState({
    nickname: '',
    email: '',
    create_date_from: '',
    create_date_to: '',
    category: []  // 빈 배열로 초기화
  });

  const navigate = useNavigate();

  // 데이터 로드 요청 (검색 또는 기본 데이터 로드)
  const loadData = useCallback(async (params) => {
    try {
      const response = await api.post('/cs/members/search', params, {
        headers: { 'Content-Type': 'application/json' }
      });
      console.log('Search results:', response.data); // 응답 데이터 로그

      if (Array.isArray(response.data)) {
        // 응답이 배열인 경우 바로 할당
        setMembers(response.data);
        setTotalMembers(response.data.length);
      } else {
        console.error('Unexpected response structure:', response.data);
        setMembers([]);
        setTotalMembers(0);
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
        const response = await api.get('/cs/members/activity', {
          params: {
            skip: (currentPage - 1) * membersPerPage,
            limit: membersPerPage,
          },
        });
  
        console.log('Fetched members:', response.data); // 응답 데이터 로그
        setMembers(Array.isArray(response.data) ? response.data : []);
        setTotalMembers(response.data.length || 0);
      } catch (error) {
        console.error('회원 정보를 불러오는데 실패했습니다.', error);
        setError('회원 정보를 불러오는데 실패했습니다.');
        setOpen(true);
      }
    };
  
    fetchInitialData();
  }, [currentPage]); // 이제 currentPage만 의존성으로 추가

  // 검색 기능을 활성화하는 POST 요청
  const handleSearch = () => {
    const params = {
      nickname: searchParams.nickname || undefined,
      email: searchParams.email || undefined,
      create_date_from: searchParams.create_date_from || undefined,
      create_date_to: searchParams.create_date_to || undefined,
      category: searchParams.category.length > 0 ? searchParams.category : undefined,
      skip: (currentPage - 1) * membersPerPage,
      limit: membersPerPage,
    };

    loadData(params);
  };

  const handleReset = () => {
    setSearchParams({
      nickname: '',
      email: '',
      create_date_from: '',
      create_date_to: '',
      category: []
    });
    setSelectedOptions({}); // 선택된 옵션 초기화
    setCurrentPage(1);
    loadData({ skip: 0, limit: membersPerPage });
  };

  const handleCategoryChange = (event) => {
    setSearchParams({ ...searchParams, category: event.target.value });
  };

  const handlePageChange = (event, value) => {
    setCurrentPage(value);
  };

  const handleCloseSnackbar = () => {
    setOpen(false); // Snackbar 닫기
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
            onChange={handleCategoryChange}
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
            {selectedOptions.create_date_from && (
              <Chip label={`가입일(시작): ${selectedOptions.create_date_from}`} />
            )}
            {selectedOptions.create_date_to && (
              <Chip label={`가입일(끝): ${selectedOptions.create_date_to}`} />
            )}
            {selectedOptions.category && selectedOptions.category.length > 0 && (
              <Chip label={`카테고리: ${selectedOptions.category.join(', ')}`} />
            )}
          </Box>
        </Box>
      )}

      {/* 데이터가 로드되지 않았거나 비어 있을 때 메시지 표시 */}
      {members.length === 0 ? (
        <Typography variant="body1">검색 결과가 없습니다.</Typography>
      ) : (
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>닉네임</TableCell>
              <TableCell>이메일</TableCell>
              <TableCell>가입일</TableCell>
              <TableCell>타입</TableCell>
              <TableCell>카테고리</TableCell>
              <TableCell>스케줄 수</TableCell>
              <TableCell>리뷰 수</TableCell>
              <TableCell>댓글 수</TableCell>
              <TableCell>예약 수</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {members.map((member) => (
              <TableRow key={member.member_id} hover>
                <TableCell onClick={() => navigate(`/members/${member.member_id}`)} style={{ cursor: 'pointer' }}>{member.member_id}</TableCell>
                <TableCell onClick={() => navigate(`/members/${member.member_id}`)} style={{ cursor: 'pointer' }}>{member.nickname}</TableCell>
                <TableCell onClick={() => navigate(`/members/${member.member_id}`)} style={{ cursor: 'pointer' }}>{member.email}</TableCell>
                <TableCell onClick={() => navigate(`/members/${member.member_id}`)} style={{ cursor: 'pointer' }}>{new Date(member.create_date).toLocaleDateString('ko-KR')}</TableCell>
                <TableCell onClick={() => navigate(`/members/${member.member_id}`)} style={{ cursor: 'pointer' }}>{member.type}</TableCell>
                <TableCell>{member.categories ? member.categories.join(', ') : 'N/A'}</TableCell>
                <TableCell
                  onClick={() => navigate('/schedules', { state: { nickname: member.nickname } })}
                  style={{ cursor: 'pointer' }}
                >
                  {member.schedules_count}
                </TableCell>
                <TableCell onClick={() => navigate(`/reviews?member_id=${member.member_id}`)} style={{ cursor: 'pointer' }}>{member.reviews_count}</TableCell>
                <TableCell onClick={() => navigate(`/comments?member_id=${member.member_id}`)} style={{ cursor: 'pointer' }}>{member.comments_count}</TableCell>
                <TableCell onClick={() => navigate(`/reservations?member_id=${member.member_id}`)} style={{ cursor: 'pointer' }}>{member.reservations_count}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      )}

      <Pagination
        count={Math.ceil(totalMembers / membersPerPage)}
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

export default Members;
