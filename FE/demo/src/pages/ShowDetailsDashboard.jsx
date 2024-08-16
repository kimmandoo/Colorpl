import React, { useState, useEffect, useCallback } from 'react';
import {
  Table, TableBody, TableCell, TableHead, TableRow, Box, Button, TextField,
  FormControl, InputLabel, Select, MenuItem, Pagination, Snackbar, Alert, Typography,
  Paper, Chip
} from '@mui/material';
import api from '../api';  // Import your Axios instance or API utility here

const ShowDetailsDashboard = () => {
  const [showDetails, setShowDetails] = useState([]);
  const [totalShows, setTotalShows] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const showsPerPage = 10;
  const [searchParams, setSearchParams] = useState({
    show_detail_api_id: '',
    show_detail_name: '',
    show_detail_area: '',
    show_detail_state: '',
    show_detail_category: '',
  });
  const [error, setError] = useState('');
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(false);

  // Function to clean up empty search parameters
  const cleanSearchParams = (params) => {
    const cleanedParams = { ...params };
    Object.keys(cleanedParams).forEach((key) => {
      if (!cleanedParams[key] || cleanedParams[key].length === 0) {
        delete cleanedParams[key];
      }
    });
    return cleanedParams;
  };

  // Fetch show details
  const fetchShowDetails = useCallback(async (params) => {
    setLoading(true);
    try {
      const response = await api.post('/vm/shows/search', params);
      if (response.status === 200) {
        setShowDetails(response.data);
        setTotalShows(response.data.length);
      } else {
        setShowDetails([]);
        setTotalShows(0);
      }
    } catch (error) {
      console.error('Error fetching show details:', error);
      setError('쇼 정보를 불러오는데 실패했습니다.');
      setOpen(true);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    const skip = (currentPage - 1) * showsPerPage;
    const params = { skip, limit: showsPerPage };

    if (Object.keys(cleanSearchParams(searchParams)).length > 0) {
      fetchShowDetails({ ...cleanSearchParams(searchParams), skip, limit: showsPerPage });
    } else {
      fetchShowDetails(params);
    }
  }, [currentPage, searchParams, fetchShowDetails]);

  const handleSearch = () => {
    setCurrentPage(1);
    fetchShowDetails({
      ...cleanSearchParams(searchParams),
      skip: 0,
      limit: showsPerPage,
    });
  };

  const handleReset = () => {
    setSearchParams({
      show_detail_api_id: '',
      show_detail_name: '',
      show_detail_area: '',
      show_detail_state: '',
      show_detail_category: '',
    });
    setCurrentPage(1);
    fetchShowDetails({ skip: 0, limit: showsPerPage });
  };

  const handlePageChange = (event, value) => {
    setCurrentPage(value);
  };

  const handleCloseSnackbar = () => {
    setOpen(false);
  };

  return (
    <Box>
      <Box sx={{ display: 'flex', gap: 2, mb: 2 }}>
        <TextField
          label="API ID"
          name="show_detail_api_id"
          value={searchParams.show_detail_api_id}
          onChange={(e) => setSearchParams({ ...searchParams, show_detail_api_id: e.target.value })}
        />
        <TextField
          label="Show Name"
          name="show_detail_name"
          value={searchParams.show_detail_name}
          onChange={(e) => setSearchParams({ ...searchParams, show_detail_name: e.target.value })}
        />
        <FormControl>
          <InputLabel>Area</InputLabel>
          <Select
            name="show_detail_area"
            value={searchParams.show_detail_area}
            onChange={(e) => setSearchParams({ ...searchParams, show_detail_area: e.target.value })}
          >
            <MenuItem value="">All</MenuItem>
            {/* Add more areas as needed */}
            <MenuItem value="서울특별시">서울특별시</MenuItem>
            <MenuItem value="부산광역시">부산광역시</MenuItem>
            {/* etc. */}
          </Select>
        </FormControl>
        <FormControl>
          <InputLabel>Status</InputLabel>
          <Select
            name="show_detail_state"
            value={searchParams.show_detail_state}
            onChange={(e) => setSearchParams({ ...searchParams, show_detail_state: e.target.value })}
          >
            <MenuItem value="">All</MenuItem>
            <MenuItem value="SCHEDULED">Scheduled</MenuItem>
            <MenuItem value="SHOWING">Ongoing</MenuItem>
            <MenuItem value="COMPLETED">Completed</MenuItem>
          </Select>
        </FormControl>
        <FormControl>
          <InputLabel>Category</InputLabel>
          <Select
            name="show_detail_category"
            value={searchParams.show_detail_category}
            onChange={(e) => setSearchParams({ ...searchParams, show_detail_category: e.target.value })}
          >
            <MenuItem value="">All</MenuItem>
            <MenuItem value="PLAY">Play</MenuItem>
            <MenuItem value="MUSICAL">Musical</MenuItem>
            {/* Add more categories as needed */}
          </Select>
        </FormControl>
        <Button variant="contained" onClick={handleSearch} disabled={loading}>Search</Button>
        <Button variant="outlined" onClick={handleReset} disabled={loading}>Reset</Button>
      </Box>

      {showDetails.length === 0 ? (
        <Typography variant="body1">No results found.</Typography>
      ) : (
        <Paper elevation={3}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>API ID</TableCell>
                <TableCell>Show Name</TableCell>
                <TableCell>Area</TableCell>
                <TableCell>Category</TableCell>
                <TableCell>Status</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {showDetails.map((show) => (
                <TableRow key={show.show_detail_id} hover>
                  <TableCell>{show.show_detail_api_id}</TableCell>
                  <TableCell>{show.show_detail_name}</TableCell>
                  <TableCell>{show.show_detail_area}</TableCell>
                  <TableCell>{show.show_detail_category}</TableCell>
                  <TableCell>{show.show_detail_state}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Paper>
      )}

      <Pagination
        count={Math.ceil(totalShows / showsPerPage)}
        page={currentPage}
        onChange={handlePageChange}
        variant="outlined"
        shape="rounded"
        sx={{ mt: 2 }}
      />
      <Snackbar open={open} autoHideDuration={6000} onClose={handleCloseSnackbar}>
        <Alert onClose={handleCloseSnackbar} severity="error">
          {error}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default ShowDetailsDashboard;
