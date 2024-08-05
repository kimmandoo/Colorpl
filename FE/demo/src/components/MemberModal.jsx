import React from 'react';
import { Modal, Box, Typography, List, ListItem } from '@mui/material';

const MemberModal = ({ open, onClose, member, onViewTicket, onViewReview, onViewComment }) => {
  if (!member) return null;

  const { recent_tickets = [], recent_reviews = [], recent_comments = [] } = member;

  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={{ width: 400, margin: 'auto', mt: 5, bgcolor: 'background.paper', p: 2, borderRadius: 2 }}>
        <Typography variant="h6" component="h2">
          {member.nickname}
        </Typography>
        <Typography variant="body1" component="p" sx={{ mt: 2 }}>
          {member.email}
        </Typography>
        <Typography variant="h6" component="h2" sx={{ mt: 2 }}>
          Recent Tickets
        </Typography>
        <List>
          {Array.isArray(recent_tickets) && recent_tickets.length > 0 ? (
            recent_tickets.map((ticket) => (
              <ListItem key={ticket.ticket_id} button onClick={() => onViewTicket(ticket.ticket_id)}>
                {ticket.ticket_name}
              </ListItem>
            ))
          ) : (
            <ListItem>No tickets available</ListItem>
          )}
        </List>
        <Typography variant="h6" component="h2" sx={{ mt: 2 }}>
          Recent Reviews
        </Typography>
        <List>
          {Array.isArray(recent_reviews) && recent_reviews.length > 0 ? (
            recent_reviews.map((review) => (
              <ListItem key={review.review_id} button onClick={() => onViewReview(review.review_id)}>
                {review.content}
              </ListItem>
            ))
          ) : (
            <ListItem>No reviews available</ListItem>
          )}
        </List>
        <Typography variant="h6" component="h2" sx={{ mt: 2 }}>
          Recent Comments
        </Typography>
        <List>
          {Array.isArray(recent_comments) && recent_comments.length > 0 ? (
            recent_comments.map((comment) => (
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

export default MemberModal;
