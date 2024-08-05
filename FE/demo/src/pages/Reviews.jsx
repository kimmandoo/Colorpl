import React, { useState, useEffect } from 'react';
import { Box, TextField, Button, Select, MenuItem } from '@mui/material';
import ReviewList from '../components/ReviewList';
import ReviewModal from '../components/ReviewModal';
import api from '../api';

const Reviews = () => {
  const [reviews, setReviews] = useState([]);
  const [searchParams, setSearchParams] = useState({ category: '', emailOrNickname: '', search: '' });
  const [selectedReview, setSelectedReview] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);
  const categories = ['Category1', 'Category2', 'Category3'];

  useEffect(() => {
    fetchReviews();
  }, []);

  const fetchReviews = async () => {
    try {
      const response = await api.get('/cs2/reviews', {
        params: { skip: 0, limit: 100 },
      });
      if (Array.isArray(response.data)) {
        setReviews(response.data);
      } else {
        setReviews([]);
      }
    } catch (error) {
      console.error(error);
      setReviews([]);
    }
  };

  const fetchReviewActivity = async (review_id) => {
    try {
      const response = await api.get(`/cs2/reviews/${review_id}/activity`, {
        params: { skip: 0, limit: 5 },
      });
      if (response.data) {
        const { review, ticket, recent_comments } = response.data;
        setSelectedReview({ ...review, ticket, recent_comments });
        // console.log(response.data);
      } else {
        setSelectedReview(null);
      }
      setModalOpen(true);
    } catch (error) {
      console.error(error);
    }
  };

  const onSearch = async () => {
    try {
      const response = await api.post('/cs2/reviews/search', {
        category: searchParams.category || null,
        email: searchParams.emailOrNickname.includes('@') ? searchParams.emailOrNickname : null,
        nickname: !searchParams.emailOrNickname.includes('@') ? searchParams.emailOrNickname : null,
        content: searchParams.search || null,
        skip: 0,
        limit: 100,
      });
      if (Array.isArray(response.data)) {
        setReviews(response.data);
      } else {
        setReviews([]);
      }
    } catch (error) {
      console.error(error);
      setReviews([]);
    }
  };

  return (
    <Box>
      <Box display="flex" alignItems="center" mb={2} gap={2}>
        <Select
          value={searchParams.category}
          onChange={(e) => setSearchParams({ ...searchParams, category: e.target.value })}
          displayEmpty
        >
          <MenuItem value="">None</MenuItem>
          {categories.map((category) => (
            <MenuItem key={category} value={category}>
              {category}
            </MenuItem>
          ))}
        </Select>
        <Select
          value={searchParams.emailOrNickname.includes('@') ? 'email' : 'nickname'}
          onChange={(e) =>
            setSearchParams({ ...searchParams, emailOrNickname: '' })
          }
        >
          <MenuItem value="email">Email</MenuItem>
          <MenuItem value="nickname">Nickname</MenuItem>
        </Select>
        <TextField
          label={searchParams.emailOrNickname.includes('@') ? 'Email' : 'Nickname'}
          value={searchParams.emailOrNickname}
          onChange={(e) => setSearchParams({ ...searchParams, emailOrNickname: e.target.value })}
          margin="normal"
        />
        <TextField
          label="Search"
          value={searchParams.search}
          onChange={(e) => setSearchParams({ ...searchParams, search: e.target.value })}
          margin="normal"
          onKeyPress={(e) => {
            if (e.key === 'Enter') {
              onSearch();
            }
          }}
        />
        <Button onClick={onSearch} variant="contained" color="primary">
          Search
        </Button>
      </Box>
      <ReviewList reviews={reviews} onReviewClick={fetchReviewActivity} />
      <ReviewModal open={modalOpen} onClose={() => setModalOpen(false)} review={selectedReview} />
    </Box>
  );
};

export default Reviews;
