import React from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Avatar, IconButton } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';

const TicketList = ({ tickets, onTicketClick }) => {
  return (
    <TableContainer>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Profile</TableCell>
            <TableCell>Title</TableCell>
            <TableCell>Category</TableCell>
            <TableCell>Theater</TableCell>
            <TableCell>Seat</TableCell>
            <TableCell>Created Date</TableCell>
            <TableCell>Actions</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {tickets.map((ticket) => (
            <TableRow key={ticket.ticket_id}>
              <TableCell>
                <Avatar src={ticket.owner.profile} alt={ticket.owner.nickname} />
              </TableCell>
              <TableCell>{ticket.ticket_name}</TableCell>
              <TableCell>{ticket.category}</TableCell>
              <TableCell>{ticket.theater}</TableCell>
              <TableCell>{ticket.seat}</TableCell>
              <TableCell>{new Date(ticket.created_date).toLocaleDateString()}</TableCell>
              <TableCell>
                <IconButton onClick={() => onTicketClick(ticket.ticket_id)}>
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

export default TicketList;
