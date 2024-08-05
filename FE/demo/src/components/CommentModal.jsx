import React from 'react';
import { Modal, Box, Typography, List, ListItem } from '@mui/material';

const CommentModal = ({ open, onClose, comment, onViewMember, onViewReview }) => {
  if (!comment) return null;

  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={{ width: 400, margin: 'auto', mt: 5, bgcolor: 'background.paper', p: 2, borderRadius: 2 }}>
        <Typography variant="h6" component="h2">
          Comment by {comment.owner?.nickname || 'unknown'}
        </Typography>
        <Typography variant="body1" component="p" sx={{ mt: 2 }}>
          {comment.commentContent}
        </Typography>
        <Typography variant="h6" component="h2" sx={{ mt: 2 }}>
          Related Review
        </Typography>
        <List>
          <ListItem button onClick={() => onViewReview(comment.review.review_id)}>
            {comment.review.content}
          </ListItem>
        </List>
        <Typography variant="h6" component="h2" sx={{ mt: 2 }}>
          Violation
        </Typography>
        <List>
          <ListItem>
            {comment.violation ? 'Yes' : 'No'}
          </ListItem>
        </List>
      </Box>
    </Modal>
  );
};

export default CommentModal;
