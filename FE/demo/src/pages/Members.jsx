import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Table, TableBody, TableCell, TableHead, TableRow, Box, Button, TextField,
  FormControl, InputLabel, Select, MenuItem, Pagination, Snackbar, Alert, Typography,
  Chip
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
  const [loading, setLoading] = useState(false);  // 요청 중 상태 관리

  const [searchParams, setSearchParams] = useState({
    nickname: '',
    email: '',
    create_date_from: '',
    create_date_to: '',
    category: []
  });

  const navigate = useNavigate();

  // 빈 값 제거 함수
  const cleanSearchParams = (params) => {
    const cleanedParams = { ...params };
    Object.keys(cleanedParams).forEach((key) => {
      if (!cleanedParams[key] || cleanedParams[key].length === 0) {
        delete cleanedParams[key];
      }
    });
    return cleanedParams;
  };

  // 데이터 로드 요청 (activity 엔드포인트)
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

  // 검색 요청 (search 엔드포인트)
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

  // 최초 렌더링 및 의존성에 따른 데이터 로드
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

  // 검색 요청 핸들러
  const handleSearch = () => {
    if (loading) return;
    setCurrentPage(1);
    fetchSearchData({
      ...searchParams,
      skip: 0,
      limit: membersPerPage,
    });
  };

  // 검색 조건 초기화 핸들러
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

  // 페이지 변경 핸들러
  const handlePageChange = (event, value) => {
    if (loading) return;
    setCurrentPage(value);
  };

  // Snackbar 닫기 핸들러
  const handleCloseSnackbar = () => {
    setOpen(false);
  };

  // 클릭 이벤트 핸들러
  const handleCellClick = (endpoint, memberId, nickname) => {
    if (loading) return;
    navigate(endpoint, {
      state: { member_id: memberId, nickname: nickname },
    });
  };

  return (
    <Box>
      {/* 검색 영역 */}
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

      {/* 선택된 검색 옵션 표시 */}
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

      {/* 데이터가 없을 때 메시지 표시 */}
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
                  onClick={() => handleCellClick('/schedules', member.member_id, member.nickname)}
                  style={{ cursor: 'pointer' }}
                >
                  {member.schedules_count}
                </TableCell>
                <TableCell 
                  onClick={() => handleCellClick('/reviews', member.member_id, member.nickname)} 
                  style={{ cursor: 'pointer' }}
                >
                  {member.reviews_count}
                </TableCell>
                <TableCell onClick={() => handleCellClick('/comments', member.member_id, member.nickname)} style={{ cursor: 'pointer' }}>{member.comments_count}</TableCell>
                <TableCell onClick={() => navigate(`/reservations?member_id=${member.member_id}`)} style={{ cursor: 'pointer' }}>{member.reservations_count}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      )}

      <Pagination
        count={maxPages}
        page={currentPage || 1} // NaN 방지를 위해 기본값 설정
        onChange={handlePageChange}
        variant="outlined"
        shape="rounded"
        showFirstButton
        showLastButton
        siblingCount={3} // 중앙에 표시될 페이지 수
        boundaryCount={2} // 양쪽 끝에 표시될 페이지 수
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
