import React from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Avatar, IconButton } from '@mui/material';
import { Search as SearchIcon } from '@mui/icons-material';

const MemberList = ({ members, onMemberClick }) => {
  return (
    <TableContainer>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Profile</TableCell>
            <TableCell>Nickname</TableCell>
            <TableCell>Email</TableCell>
            <TableCell>Category</TableCell>
            <TableCell>Created At</TableCell>
            <TableCell>Modified At</TableCell>
            <TableCell>Details</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {members.map((member) => (
            <TableRow key={member.member_id}>
              <TableCell>
                <Avatar src={member.profile} alt={member.nickname} />
              </TableCell>
              <TableCell>{member.nickname}</TableCell>
              <TableCell>{member.email}</TableCell>
              <TableCell>{member.categories.map(category => category.name).join(', ')}</TableCell>
              <TableCell>{new Date(member.created_date).toLocaleDateString()}</TableCell>
              <TableCell>{new Date(member.modified_date).toLocaleDateString()}</TableCell>
              <TableCell>
                <IconButton onClick={() => onMemberClick(member.member_id)}>
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

export default MemberList;
