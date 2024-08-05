import React from 'react';
import { Modal, Box, Typography, List, ListItem } from '@mui/material';

const TicketModal = ({ open, onClose, ticket }) => {
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
      </Box>
    </Modal>
  );
};

export default TicketModal;
