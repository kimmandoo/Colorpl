// pages/Comments.jsx
import React, { useState, useEffect } from 'react';
import { Box, TextField, Button, Select, MenuItem } from '@mui/material';
import CommentList from '../components/CommentList';
import CommentModal from '../components/CommentModal';
import api from '../api';

const Comments = () => {
  const [comments, setComments] = useState([]);
  const [searchParams, setSearchParams] = useState({ category: '', emailOrNickname: '', search: '' });
  const [selectedComment, setSelectedComment] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);
  const categories = ['Category1', 'Category2', 'Category3'];

  useEffect(() => {
    fetchComments();
  }, []);

  const fetchComments = async () => {
    try {
      const response = await api.get('/cs2/comments', {
        params: { skip: 0, limit: 100 },
      });
      if (Array.isArray(response.data)) {
        setComments(response.data);
      } else {
        setComments([]);
      }
    } catch (error) {
      console.error(error);
      setComments([]);
    }
  };

  const fetchCommentActivity = async (comment_id) => {
    try {
      const response = await api.get(`/cs2/comments/${comment_id}/activity`, {
        params: { skip: 0, limit: 5 },
      });
      setSelectedComment(response.data);
      setModalOpen(true);
    } catch (error) {
      console.error(error);
    }
  };

  const onSearch = async () => {
    try {
      const response = await api.post('/cs2/comments/search', {
        category: searchParams.category || null,
        email: searchParams.emailOrNickname.includes('@') ? searchParams.emailOrNickname : null,
        nickname: !searchParams.emailOrNickname.includes('@') ? searchParams.emailOrNickname : null,
        content: searchParams.search || null,
        skip: 0,
        limit: 100,
      });
      if (Array.isArray(response.data)) {
        setComments(response.data);
      } else {
        setComments([]);
      }
    } catch (error) {
      console.error(error);
      setComments([]);
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
      <CommentList comments={comments} onCommentClick={fetchCommentActivity} />
      <CommentModal open={modalOpen} onClose={() => setModalOpen(false)} comment={selectedComment} />
    </Box>
  );
};

export default Comments;
