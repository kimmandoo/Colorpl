import React from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Avatar, IconButton, Paper } from '@mui/material';
import { Search as SearchIcon } from '@mui/icons-material';

const CommentList = ({ comments, onCommentClick }) => {
  return (
    <TableContainer component={Paper} sx={{ width: '100%', overflowX: 'auto' }}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Profile</TableCell>
            <TableCell>Nickname</TableCell>
            <TableCell>Email</TableCell>
            <TableCell>Content</TableCell>
            <TableCell>Created At</TableCell>
            <TableCell>Modified At</TableCell>
            <TableCell>Violation</TableCell>
            <TableCell>Details</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {comments.map((comment) => (
            <TableRow key={comment.comment_id}>
              <TableCell>
                <Avatar src={comment.owner.profile} alt={comment.owner.nickname} />
              </TableCell>
              <TableCell>{comment.owner.nickname}</TableCell>
              <TableCell>{comment.owner.email}</TableCell>
              <TableCell>{comment.commentContent}</TableCell>
              <TableCell>{new Date(comment.created_date).toLocaleDateString()}</TableCell>
              <TableCell>{new Date(comment.modified_date).toLocaleDateString()}</TableCell>
              <TableCell>{comment.violation ? 'Yes' : 'No'}</TableCell>
              <TableCell>
                <IconButton onClick={() => onCommentClick(comment.comment_id)}>
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

export default CommentList;
