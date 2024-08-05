// components/MemberList.jsx
import React from 'react';
import { List, ListItem, ListItemText, ListItemAvatar, Avatar, IconButton } from '@mui/material';
import { Search as SearchIcon } from '@mui/icons-material';

const MemberList = ({ members, onMemberClick }) => {
  return (
    <List>
      {members.map((member) => (
        <ListItem 
          key={member.member_id} 
          secondaryAction={
            <IconButton edge="end" aria-label="search" onClick={() => onMemberClick(member.member_id)}>
              <SearchIcon />
            </IconButton>
          }
        >
          <ListItemAvatar>
            <Avatar src={member.profile} alt={member.nickname} />
          </ListItemAvatar>
          <ListItemText
            primary={member.nickname}
            secondary={
              <>
                <span>Email: {member.email}</span><br />
                <span>
                  Categories: {member.categories && member.categories.length > 0
                    ? member.categories.map(category => category.name).join(', ')
                    : 'No categories'}
                </span>
              </>
            }
          />
        </ListItem>
      ))}
    </List>
  );
};

export default MemberList;
