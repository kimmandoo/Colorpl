// CommentModal.js
import React from 'react';
import { Modal, Box, Typography, List, ListItem, ListItemText } from '@mui/material';

const CommentModal = ({ open, onClose, comment }) => {
  if (!comment) return null;

  const owner = comment.owner || {};
  const review = comment.review || {};

  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={{ p: 4, bgcolor: 'background.paper', margin: 'auto', mt: '10%', maxWidth: 600 }}>
        <Typography variant="h6" component="h2">
          {/* {comment.commentContent}의 활동 */}
          {owner.nickname}의 활동
        </Typography>
        <Typography variant="subtitle1">리뷰</Typography>
        <List>
          <ListItem key={review.review_id}>
            <ListItemText primary={review.content || 'Unknown Review'} />
          </ListItem>
        </List>
        <Typography variant="subtitle1">작성자</Typography>
        <List>
          <ListItem key={owner.member_id}>
            <ListItemText primary={owner.nickname || 'Unknown Writer'} />
          </ListItem>
        </List>
      </Box>
    </Modal>
  );
};

export default CommentModal;
