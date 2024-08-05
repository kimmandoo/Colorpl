import React from 'react';
import { Modal, Box, Typography, List, ListItem } from '@mui/material';

const ReviewModal = ({ open, onClose, review }) => {
  if (!review) return null;

  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={{ width: 400, margin: 'auto', mt: 5, bgcolor: 'background.paper', p: 2, borderRadius: 2 }}>
        <Typography variant="h6" component="h2">
          작성자 : {review.owner?.nickname || 'Unknown'}
        </Typography>
        {review.ticket && (
          <>
            <Typography variant="h6" component="h2" sx={{ mt: 2 }}>
              리뷰 타이틀 :
            </Typography>
            <List>
              <ListItem key={review.ticket.ticket_id}>
                {review.ticket.ticket_name}
              </ListItem>
            </List>
          </>
        )}
        <Typography variant="body1" component="p" sx={{ mt: 2 }}>
          리뷰 내용:
          {review.content || 'No content available'}
        </Typography>
        <Typography variant="h6" component="h2" sx={{ mt: 2 }}>
          댓글 :
        </Typography>
        <List>
          {review.recent_comments && review.recent_comments.length > 0 ? (
            review.recent_comments.map((comment) => (
              <ListItem key={comment.comment_id}>
                {comment.commentContent}
              </ListItem>
            ))
          ) : (
            <ListItem>No comments available</ListItem>
          )}
        </List>
      </Box>
    </Modal>
  );
};

export default ReviewModal;
