import React from 'react';
import { List, ListItem, ListItemText, ListItemAvatar, Avatar, IconButton } from '@mui/material';
import { Search as SearchIcon } from '@mui/icons-material';

const ReviewList = ({ reviews, onReviewClick }) => {
  return (
    <List>
      {reviews.map((review) => {
        const owner = review.owner || {};
        return (
          <ListItem key={review.review_id} secondaryAction={
            <IconButton edge="end" aria-label="search" onClick={() => onReviewClick(review.review_id)}>
              <SearchIcon />
            </IconButton>
          }>
            <ListItemAvatar>
              <Avatar src={owner.profile} alt={owner.nickname} />
            </ListItemAvatar>
            <ListItemText
              primary={review.content}
              secondary={
                <>
                  <span>후기: {review.emotion}</span><br />
                  <span>작성자: {owner.nickname}</span>
                </>
              }
            />
          </ListItem>
        );
      })}
    </List>
  );
};

export default ReviewList;
