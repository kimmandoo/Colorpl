import React, { useState, useEffect, useCallback } from 'react';
import { Box, TextField, Button, Table, TableBody, TableCell, TableHead, TableRow, Pagination, TableContainer, Paper, Typography } from '@mui/material';
import api from '../api';

const TheatersTablePage = () => {
  const [searchTerm, setSearchTerm] = useState('');
  const [theaters, setTheaters] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const theatersPerPage = 10;
  const [totalTheaters, setTotalTheaters] = useState(0);
  const [isSearchMode, setIsSearchMode] = useState(false);
  const maxPages = 100;

  const cleanSearchParams = (params) => {
    const cleanedParams = { ...params };
    Object.keys(cleanedParams).forEach((key) => {
      if (!cleanedParams[key]) {
        delete cleanedParams[key];
      }
    });
    return cleanedParams;
  };

  const fetchTheaters = useCallback(async () => {
    try {
      const skip = (currentPage - 1) * theatersPerPage;
      const endpoint = isSearchMode ? '/vm/theaters/search' : '/vm/theaters';
      
      let response;
      if (isSearchMode) {
        const params = cleanSearchParams({ theater_name: searchTerm, skip, limit: theatersPerPage });
        response = await api.post(endpoint, params, { headers: { 'Content-Type': 'application/json' } });
      } else {
        response = await api.get(endpoint, { params: { skip, limit: theatersPerPage } });
      }
  
      setTheaters(response.data);
      setTotalTheaters(response.data.length > 0 ? response.data[0].total_count : 0);
    } catch (error) {
      console.error('Error fetching theaters:', error);
      setTheaters([]);
    }
  }, [currentPage, searchTerm, isSearchMode]);

  useEffect(() => {
    fetchTheaters();
  }, [fetchTheaters]);

  const handleSearch = () => {
    setIsSearchMode(true);
    setCurrentPage(1);
    fetchTheaters();
  };

  const handleReset = () => {
    setSearchTerm('');
    setIsSearchMode(false);
    setCurrentPage(1);
    fetchTheaters();
  };

  const handlePageChange = (event, value) => {
    setCurrentPage(value);
  };

  return (
    <Box>
      <Box sx={{ display: 'flex', gap: 2, mb: 2 }}>
        <TextField
          label="극장 이름 검색"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <Button variant="contained" onClick={handleSearch}>검색</Button>
        <Button variant="outlined" onClick={handleReset}>초기화</Button>
      </Box>

      {theaters.length === 0 ? (
        <Box sx={{ textAlign: 'center', mt: 2 }}>
          <Typography variant="body1">검색 결과가 없습니다.</Typography>
        </Box>
      ) : (
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>API ID</TableCell>
                <TableCell>이름</TableCell>
                <TableCell>주소</TableCell>
                <TableCell>홀 갯수</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {theaters.map((theater) => (
                <TableRow key={theater.theater_id}>
                  <TableCell>{theater.theater_api_id}</TableCell>
                  <TableCell>{theater.theater_name}</TableCell>
                  <TableCell>{theater.theater_address}</TableCell>
                  <TableCell>{theater.halls.length}</TableCell> {/* 홀 갯수 표시 */}
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
    </Box>
  );
};

export default TheatersTablePage;
