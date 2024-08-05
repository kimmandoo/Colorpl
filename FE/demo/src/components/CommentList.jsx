import React from 'react';
import { List, ListItem, ListItemText, ListItemAvatar, Avatar, IconButton } from '@mui/material';
import { Search as SearchIcon } from '@mui/icons-material';

const CommentList = ({ comments, onCommentClick }) => {
  return (
    <List>
      {comments.map((comment) => {
        const owner = comment.owner || {};
        return (
          <ListItem key={comment.comment_id} secondaryAction={
            <IconButton edge="end" aria-label="search" onClick={() => onCommentClick(comment.comment_id)}>
              <SearchIcon />
            </IconButton>
          }>
            <ListItemAvatar>
              <Avatar src={owner.profile} alt={owner.nickname} />
            </ListItemAvatar>
            <ListItemText
              primary={comment.commentContent}
              secondary={
                <>
                  <span>Writer: {owner.nickname}</span><br />
                  <span>Review ID: {comment.review_id}</span>
                </>
              }
            />
          </ListItem>
        );
      })}
    </List>
  );
};

export default CommentList;
