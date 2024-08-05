import React from 'react';
import { Modal, Box, Typography, List, ListItem, ListItemText } from '@mui/material';

const TicketModal = ({ open, onClose, ticket, onViewMember, onViewReview }) => {
  if (!ticket) return null;

  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={{ width: 400, margin: 'auto', mt: 5, bgcolor: 'background.paper', p: 2, borderRadius: 2 }}>
        <Typography variant="h6" component="h2">
          Ticket: {ticket.ticket_name}
        </Typography>
        <Typography variant="body1" component="p" sx={{ mt: 2 }}>
          Theater: {ticket.theater}
        </Typography>
        <Typography variant="body1" component="p" sx={{ mt: 2 }}>
          Seat: {ticket.seat}
        </Typography>
        <Typography variant="body1" component="p" sx={{ mt: 2 }}>
          Category: {ticket.category}
        </Typography>
        <Typography variant="h6" component="h2" sx={{ mt: 2 }}>
          Owner
        </Typography>
        <List>
          <ListItem button onClick={() => onViewMember(ticket.owner.member_id)}>
            <ListItemText primary={ticket.owner.nickname} secondary={ticket.owner.email} />
          </ListItem>
        </List>
        <Typography variant="h6" component="h2" sx={{ mt: 2 }}>
          Reviews
        </Typography>
        <List>
          {ticket.recent_reviews && ticket.recent_reviews.length > 0 ? (
            ticket.recent_reviews.map((review) => (
              <ListItem button key={review.review_id} onClick={() => onViewReview(review.review_id)}>
                <ListItemText primary={review.content} secondary={`By: ${review.owner.nickname}`} />
              </ListItem>
            ))
          ) : (
            <ListItem>No reviews available</ListItem>
          )}
        </List>
      </Box>
    </Modal>
  );
};

export default TicketModal;
