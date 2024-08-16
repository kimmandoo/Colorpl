import React, { useState } from 'react';
import { Grid, TextField, Button, Typography, Select, MenuItem, FormControl, InputLabel, Checkbox, FormControlLabel } from '@mui/material';
import api from '../api';

const ShowDetailPage = ({ onShowDetailSubmit, selectedHallId }) => {
  const [showDetail, setShowDetail] = useState({
    show_detail_id: null,
    show_detail_api_id: '',
    show_detail_area: '',
    show_detail_cast: '',
    show_detail_category: '',
    show_detail_name: '',
    show_detail_poster_image_path: '',
    show_detail_runtime: '',
    show_detail_state: '',
    hall_id: selectedHallId || null,
  });

  const [searchTerm, setSearchTerm] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [manualJsonInput, setManualJsonInput] = useState('');

  const handleChange = (e) => {
    setShowDetail({
      ...showDetail,
      [e.target.name]: e.target.value,
    });
  };

  const handleManualInputChange = (e) => {
    setManualJsonInput(e.target.value);
  };

  const handleSearch = async () => {
    try {
      const response = await api.post('/vm/shows/search', { show_detail_name: searchTerm });
      setSearchResults(response.data);
    } catch (error) {
      console.error('Error searching shows', error);
    }
  };

  const handleSelectShow = (selectedShow) => {
    setShowDetail({
      ...showDetail,
      show_detail_api_id: selectedShow.show_detail_api_id,
      show_detail_area: selectedShow.show_detail_area,
      show_detail_cast: selectedShow.show_detail_cast,
      show_detail_category: selectedShow.show_detail_category,
      show_detail_name: selectedShow.show_detail_name,
      show_detail_poster_image_path: selectedShow.show_detail_poster_image_path,
      show_detail_runtime: selectedShow.show_detail_runtime,
      show_detail_state: selectedShow.show_detail_state,
      hall_id: selectedHallId || selectedShow.hall_id,
    });
  };

  const handleSubmit = async () => {
    try {
      let dataToSubmit;
      if (manualJsonInput) {
        const jsonParsed = JSON.parse(manualJsonInput);
        dataToSubmit = {
          ...jsonParsed,
          show_detail_area: showDetail.show_detail_area || jsonParsed.show_detail_area,
          show_detail_category: showDetail.show_detail_category || jsonParsed.show_detail_category,
          show_detail_state: showDetail.show_detail_state || jsonParsed.show_detail_state,
          hall_id: selectedHallId || jsonParsed.hall_id,
        };
      } else {
        dataToSubmit = showDetail;
      }

      const response = await api.post('/vm/shows', dataToSubmit);
      onShowDetailSubmit(response.data);
    } catch (error) {
      console.error('Error creating show detail', error);
      alert('Invalid input.');
    }
  };

  return (
    <Grid container spacing={2} sx={{ padding: 4 }}>
      <Grid item xs={12}>
        <Typography variant="h4">공연 등록</Typography>
      </Grid>

      {/* 좌측: 검색 및 폼 데이터 */}
      <Grid item xs={6}>
        <div>
          <Typography variant="h6">입력 폼</Typography>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <TextField
              label="Search Show by Name"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              fullWidth
            />
            <Button onClick={handleSearch} variant="contained" sx={{ mt: 1 }}>
              검색
            </Button>
          </FormControl>

          <ul>
            {searchResults.map((show) => (
              <li key={show.show_detail_id} onClick={() => handleSelectShow(show)}>
                {show.show_detail_name}
              </li>
            ))}
          </ul>

          <div>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                label="Show API ID"
                name="show_detail_api_id"
                value={showDetail.show_detail_api_id}
                onChange={handleChange}
                fullWidth
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <InputLabel>Show Area</InputLabel>
              <Select
                name="show_detail_area"
                value={showDetail.show_detail_area}
                onChange={handleChange}
                fullWidth
              >
                <MenuItem value="">Select Area</MenuItem>
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
                {/* 다른 지역 옵션 */}
              </Select>
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                label="Show Cast"
                name="show_detail_cast"
                value={showDetail.show_detail_cast}
                onChange={handleChange}
                fullWidth
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <InputLabel>Show Genre</InputLabel>
              <Select
                name="show_detail_category"
                value={showDetail.show_detail_category}
                onChange={handleChange}
                fullWidth
              >
                <MenuItem value="">Select Genre</MenuItem>
                <MenuItem value="PLAY">연극</MenuItem>
                <MenuItem value="MOVIE">영화</MenuItem>
                <MenuItem value="PERFORMANCE">공연</MenuItem>
                <MenuItem value="CONCERT">콘서트</MenuItem>
                <MenuItem value="MUSICAL">뮤지컬</MenuItem>
                <MenuItem value="EXHIBITION">전시회</MenuItem>
                <MenuItem value="ETC">기타</MenuItem>
                {/* 다른 장르 옵션 */}
              </Select>
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                label="Show Name"
                name="show_detail_name"
                value={showDetail.show_detail_name}
                onChange={handleChange}
                fullWidth
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                label="Show Poster URL"
                name="show_detail_poster_image_path"
                value={showDetail.show_detail_poster_image_path}
                onChange={handleChange}
                fullWidth
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                label="Show Runtime"
                name="show_detail_runtime"
                value={showDetail.show_detail_runtime}
                onChange={handleChange}
                fullWidth
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <InputLabel>Show State</InputLabel>
              <Select
                name="show_detail_state"
                value={showDetail.show_detail_state}
                onChange={handleChange}
                fullWidth
              >
                <MenuItem value="">Select State</MenuItem>
                <MenuItem value="COMPLETED">완료</MenuItem>
                <MenuItem value="SCHEDULED">예정</MenuItem>
                <MenuItem value="SHOWING">공연 중</MenuItem>
              </Select>
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <TextField
                label="Hall ID"
                name="hall_id"
                value={showDetail.hall_id || ''}
                disabled
                fullWidth
              />
            </FormControl>
          </div>
        </div>
      </Grid>

      {/* 우측: JSON 입력 */}
      <Grid item xs={6}>
        <Typography variant="h6">JSON 입력</Typography>
        <TextField
          label="Manual JSON Input"
          multiline
          rows={10}
          value={manualJsonInput}
          onChange={handleManualInputChange}
          placeholder={`{
  "show_detail_api_id": "PF236460",
  "show_detail_cast": "Actor Name",
  "show_detail_name": "Show Name",
  "show_detail_poster_image_path": "http://example.com/poster.jpg",
  "show_detail_runtime": "2시간",
  "hall_id": ${selectedHallId || null}
}`}
          fullWidth
          variant="outlined"
        />
        <Button onClick={handleSubmit} variant="contained" sx={{ mt: 2 }}>
          Create Show Detail
        </Button>
      </Grid>
    </Grid>
  );
};

export default ShowDetailPage;
