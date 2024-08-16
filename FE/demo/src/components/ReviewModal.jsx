import React from 'react';
import { Modal, Box, Typography, List, ListItem } from '@mui/material';

const ReviewModal = ({ open, onClose, review, onViewMember, onViewTicket, onViewComment }) => {
  if (!review) return null;

  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={{ width: 400, margin: 'auto', mt: 5, bgcolor: 'background.paper', p: 2, borderRadius: 2 }}>
        <Typography variant="h6" component="h2">
          Review by {review.owner?.nickname || 'unknown'}
        </Typography>
        <Typography variant="body1" component="p" sx={{ mt: 2 }}>
          {review.content}
        </Typography>
        <Typography variant="h6" component="h2" sx={{ mt: 2 }}>
          Related Ticket
        </Typography>
        <List>
          <ListItem button onClick={() => onViewTicket(review.ticket.ticket_id)}>
            {review.ticket.ticket_name}
          </ListItem>
        </List>
        <Typography variant="h6" component="h2" sx={{ mt: 2 }}>
          Comments
        </Typography>
        <List>
          {Array.isArray(review.recent_comments) && review.recent_comments.length > 0 ? (
            review.recent_comments.map((comment) => (
              <ListItem key={comment.comment_id} button onClick={() => onViewComment(comment.comment_id)}>
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
