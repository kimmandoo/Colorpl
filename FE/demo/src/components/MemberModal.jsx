import React from 'react';
import { Modal, Box, Typography, List, ListItem, ListItemText } from '@mui/material';

const MemberModal = ({ open, onClose, member }) => {
  if (!member) return null;

  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={{ p: 4, bgcolor: 'background.paper', margin: 'auto', mt: '10%', maxWidth: 600 }}>
        <Typography variant="h6" component="h2">
          {member.member.nickname}의 활동
        </Typography>
        <Typography variant="subtitle1">최근 티켓</Typography>
        <List>
          {member.recent_tickets.map(ticket => (
            <ListItem key={ticket.ticket_id}>
              <ListItemText primary={ticket.ticket_name} />
            </ListItem>
          ))}
        </List>
        <Typography variant="subtitle1">최근 리뷰</Typography>
        <List>
          {member.recent_reviews.map(review => (
            <ListItem key={review.review_id}>
              <ListItemText primary={review.content} />
            </ListItem>
          ))}
        </List>
        <Typography variant="subtitle1">최근 댓글</Typography>
        <List>
          {member.recent_comments.map(comment => (
            <ListItem key={comment.comment_id}>
              <ListItemText primary={comment.commentContent} />
            </ListItem>
          ))}
        </List>
      </Box>
    </Modal>
  );
};

export default MemberModal;
