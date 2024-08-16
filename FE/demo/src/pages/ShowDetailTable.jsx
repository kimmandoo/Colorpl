import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Table, TableBody, TableCell, TableHead, TableRow, Box, Button, TextField,
  FormControl, InputLabel, Select, MenuItem, Pagination, Snackbar, Alert, Typography,
  Chip, Paper
} from '@mui/material';
import api from '../api';

const ShowDetailsTable = () => {
  const [showDetails, setShowDetails] = useState([]);
  const [totalShows, setTotalShows] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const showsPerPage = 10;
  const maxPages = 100;
  const [error, setError] = useState('');
  const [open, setOpen] = useState(false);
  const [selectedOptions, setSelectedOptions] = useState({});
  const [loading, setLoading] = useState(false);

  const [searchParams, setSearchParams] = useState({
    show_detail_api_id: '',
    show_detail_name: '',
    show_detail_area: '',
    show_detail_state: '',
    show_detail_category: '',
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

  const fetchShowDetails = useCallback(async (params) => {
    setLoading(true);
    try {
        const cleanedParams = cleanSearchParams(params);
        const response = await api.post('/vm/shows/search', cleanedParams);
        if (Array.isArray(response.data)) {
            setShowDetails(response.data);
            setTotalShows(response.data.length > 0 ? response.data[0].total_count : 0);
        } else {
            setShowDetails([]);
            setTotalShows(0);
        }
    } catch (error) {
        console.error('Show details could not be fetched.', error);
        console.error('Request data:', params);
        setError('쇼 정보를 불러오는데 실패했습니다.');
        setOpen(true);
    } finally {
        setLoading(false);
    }
}, []);

  useEffect(() => {
    if (loading) return;

    const skip = (currentPage - 1) * showsPerPage;
    const params = { skip, limit: showsPerPage };

    if (Object.keys(selectedOptions).length > 0) {
      fetchShowDetails({ ...selectedOptions, skip, limit: showsPerPage });
    } else {
      fetchShowDetails(params);
    }
  }, [currentPage, selectedOptions, fetchShowDetails]);

  const handleSearch = () => {
    if (loading) return;
    setCurrentPage(1);
    fetchShowDetails({
      ...searchParams,
      skip: 0,
      limit: showsPerPage,
    });
  };

  const handleReset = () => {
    if (loading) return;
    setSearchParams({
      show_detail_api_id: '',
      show_detail_name: '',
      show_detail_area: '',
      show_detail_state: '',
      show_detail_category: '',
    });
    setSelectedOptions({});
    setCurrentPage(1);
    fetchShowDetails({ skip: 0, limit: showsPerPage });
  };

  const handlePageChange = (event, value) => {
    if (loading) return;
    setCurrentPage(value);
  };

  const handleCloseSnackbar = () => {
    setOpen(false);
  };

  const handleRowClick = (showDetailId) => {
    if (loading) return;
    navigate(`/shows/${showDetailId}`);
  };

  return (
    <Box>
      <Box sx={{ display: 'flex', gap: 2, mb: 2, alignItems: 'center' }}>
        <TextField
          label="API ID"
          value={searchParams.show_detail_api_id}
          onChange={(e) => setSearchParams({ ...searchParams, show_detail_api_id: e.target.value })}
          sx={{ width: '150px' }}
        />
        <TextField
          label="쇼 이름"
          value={searchParams.show_detail_name}
          onChange={(e) => setSearchParams({ ...searchParams, show_detail_name: e.target.value })}
          sx={{ width: '200px' }}
        />
        <FormControl sx={{ width: '150px' }}>
          <InputLabel>지역</InputLabel>
          <Select
            value={searchParams.show_detail_area}
            onChange={(e) => setSearchParams({ ...searchParams, show_detail_area: e.target.value })}
          >
            <MenuItem value="">전체</MenuItem>
            <MenuItem value="서울특별시">서울특별시</MenuItem>
            <MenuItem value="부산광역시">부산광역시</MenuItem>
            <MenuItem value="대구광역시">대구광역시</MenuItem>
            <MenuItem value="인천광역시">인천광역시</MenuItem>
            <MenuItem value="광주광역시">광주광역시</MenuItem>
            <MenuItem value="대전광역시">대전광역시</MenuItem>
            <MenuItem value="울산광역시">울산광역시</MenuItem>
            <MenuItem value="세종특별자치시">세종특별자치시</MenuItem>
            <MenuItem value="경기도">경기도</MenuItem>
            <MenuItem value="강원특별자치도">강원특별자치도</MenuItem>
            <MenuItem value="충청북도">충청북도</MenuItem>
            <MenuItem value="충청남도">충청남도</MenuItem>
            <MenuItem value="전라북도">전라북도</MenuItem>
            <MenuItem value="전라남도">전라남도</MenuItem>
            <MenuItem value="경상북도">경상북도</MenuItem>
            <MenuItem value="경상남도">경상남도</MenuItem>
            <MenuItem value="제주특별자치도">제주특별자치도</MenuItem>
            <MenuItem value="해외 기타 지역">해외 기타 지역</MenuItem>
            {/* 필요한 추가 지역 옵션 */}
          </Select>
        </FormControl>
        <FormControl sx={{ width: '150px' }}>
          <InputLabel>상태</InputLabel>
          <Select
            value={searchParams.show_detail_state}
            onChange={(e) => setSearchParams({ ...searchParams, show_detail_state: e.target.value })}
          >
            <MenuItem value="">전체</MenuItem>
            <MenuItem value="COMPLETED">완료</MenuItem>
            <MenuItem value="SCHEDULED">예정</MenuItem>
            <MenuItem value="SHOWING">공연 중</MenuItem>
            {/* 필요한 추가 상태 옵션 */}
          </Select>
        </FormControl>
        <FormControl sx={{ width: '150px' }}>
          <InputLabel>장르</InputLabel>
          <Select
            value={searchParams.show_detail_category}
            onChange={(e) => setSearchParams({ ...searchParams, show_detail_category: e.target.value })}
          >
            <MenuItem value="">전체</MenuItem>
            <MenuItem value="PLAY">연극</MenuItem>
            <MenuItem value="MUSICAL">뮤지컬</MenuItem>
            <MenuItem value="PERFORMANCE">공연</MenuItem>
            <MenuItem value="CONCERT">콘서트</MenuItem>
            <MenuItem value="MUSICAL">뮤지컬</MenuItem>
            <MenuItem value="EXHIBITION">전시회</MenuItem>
            <MenuItem value="ETC">기타</MenuItem>
            {/* 필요한 추가 장르 옵션 */}
          </Select>
        </FormControl>
        <Button variant="contained" onClick={handleSearch} sx={{ width: '100px' }} disabled={loading}>검색</Button>
        <Button variant="outlined" onClick={handleReset} sx={{ width: '100px' }} disabled={loading}>초기화</Button>
      </Box>

      {Object.keys(selectedOptions).length > 0 && (
        <Box sx={{ mb: 2 }}>
          <Typography variant="subtitle1">선택된 검색 옵션:</Typography>
          <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
            {selectedOptions.show_detail_api_id && <Chip label={`API ID: ${selectedOptions.show_detail_api_id}`} />}
            {selectedOptions.show_detail_name && <Chip label={`쇼 이름: ${selectedOptions.show_detail_name}`} />}
            {selectedOptions.show_detail_area && <Chip label={`지역: ${selectedOptions.show_detail_area}`} />}
            {selectedOptions.show_detail_state && <Chip label={`상태: ${selectedOptions.show_detail_state}`} />}
            {selectedOptions.show_detail_category && <Chip label={`장르: ${selectedOptions.show_detail_category}`} />}
          </Box>
        </Box>
      )}

      {showDetails.length === 0 ? (
        <Typography variant="body1">검색 결과가 없습니다.</Typography>
      ) : (
        <Paper elevation={3}>
          <Table sx={{ minWidth: 650 }}>
            <TableHead>
              <TableRow>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>API ID</TableCell>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>쇼 이름</TableCell>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>지역</TableCell>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>장르</TableCell>
                <TableCell sx={{ fontWeight: 'bold', backgroundColor: '#f5f5f5' }}>상태</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {showDetails.map((showDetail) => (
                <TableRow
                  key={showDetail.show_detail_id}
                  hover
                  onClick={() => handleRowClick(showDetail.show_detail_id)}
                  sx={{ cursor: 'pointer' }}
                >
                  <TableCell>{showDetail.show_detail_api_id}</TableCell>
                  <TableCell>{showDetail.show_detail_name}</TableCell>
                  <TableCell>{showDetail.show_detail_area}</TableCell>
                  <TableCell>{showDetail.show_detail_category}</TableCell>
                  <TableCell>{showDetail.show_detail_state}</TableCell>
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

export default ShowDetailsTable;
