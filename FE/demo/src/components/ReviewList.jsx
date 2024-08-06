import React from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Avatar, IconButton, Paper } from '@mui/material';
import { Search as SearchIcon } from '@mui/icons-material';

const ReviewList = ({ reviews, onReviewClick }) => {
  const truncateContent = (content) => {
    if (content.length > 50) {
      return `${content.slice(0, 50)}...`;
    }
    return content;
  };

  return (
    <TableContainer component={Paper} sx={{ width: '100%', overflowX: 'auto' }}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Profile</TableCell>
            <TableCell>Author</TableCell>
            <TableCell>Date</TableCell>
            <TableCell>Emotion</TableCell>
            <TableCell>Content</TableCell>
            <TableCell>Spoiler</TableCell>
            <TableCell>Details</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {reviews.map((review) => (
            <TableRow key={review.review_id}>
              <TableCell>
                <Avatar src={review.owner.profile} alt={review.owner.nickname} />
              </TableCell>
              <TableCell>{review.owner.nickname}</TableCell>
              <TableCell>{new Date(review.created_date).toLocaleDateString()}</TableCell>
              <TableCell>{review.emotion}</TableCell>
              <TableCell>{truncateContent(review.content)}</TableCell>
              <TableCell>{review.spoiler ? 'Yes' : 'No'}</TableCell>
              <TableCell>
                <IconButton onClick={() => onReviewClick(review.review_id)}>
                  <SearchIcon />
                </IconButton>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default ReviewList;
