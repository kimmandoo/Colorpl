import React, { useState, useEffect } from 'react';
import { Box, TextField, Button, Select, MenuItem } from '@mui/material';
import TicketList from '../components/TicketList';
import TicketModal from '../components/TicketModal';
import api from '../api';

const Tickets = ({ onViewMember, onViewReview }) => {
  const [tickets, setTickets] = useState([]);
  const [searchParams, setSearchParams] = useState({ category: '', emailOrNickname: '', search: '' });
  const [selectedTicket, setSelectedTicket] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);
  const categories = ['Category1', 'Category2', 'Category3'];

  useEffect(() => {
    fetchTickets();
  }, []);

  const fetchTickets = async () => {
    try {
      const response = await api.get('/cs2/tickets', {
        params: { skip: 0, limit: 100 },
      });
      if (Array.isArray(response.data)) {
        setTickets(response.data);
      } else {
        setTickets([]);
      }
    } catch (error) {
      console.error(error);
      setTickets([]);
    }
  };

  const fetchTicketActivity = async (ticket_id) => {
    try {
      const response = await api.get(`/cs2/tickets/${ticket_id}/activity`, {
        params: { skip: 0, limit: 5 },
      });
      setSelectedTicket(response.data);
      setModalOpen(true);
    } catch (error) {
      console.error(error);
    }
  };

  const onSearch = async () => {
    try {
      const response = await api.post('/cs2/tickets/search', {
        category: searchParams.category || null,
        email: searchParams.emailOrNickname.includes('@') ? searchParams.emailOrNickname : null,
        nickname: !searchParams.emailOrNickname.includes('@') ? searchParams.emailOrNickname : null,
        search: searchParams.search || null,
        skip: 0,
        limit: 100,
      });
      if (Array.isArray(response.data)) {
        setTickets(response.data);
      } else {
        setTickets([]);
      }
    } catch (error) {
      console.error(error);
      setTickets([]);
    }
  };

  return (
    <Box>
      <Box display="flex" alignItems="center" mb={2} gap={2}>
        <Select
          value={searchParams.category}
          onChange={(e) => setSearchParams({ ...searchParams, category: e.target.value })}
          displayEmpty
        >
          <MenuItem value="">None</MenuItem>
          {categories.map((category) => (
            <MenuItem key={category} value={category}>
              {category}
            </MenuItem>
          ))}
        </Select>
        <Select
          value={searchParams.emailOrNickname.includes('@') ? 'email' : 'nickname'}
          onChange={(e) =>
            setSearchParams({ ...searchParams, emailOrNickname: '' })
          }
        >
          <MenuItem value="email">Email</MenuItem>
          <MenuItem value="nickname">Nickname</MenuItem>
        </Select>
        <TextField
          label={searchParams.emailOrNickname.includes('@') ? 'Email' : 'Nickname'}
          value={searchParams.emailOrNickname}
          onChange={(e) => setSearchParams({ ...searchParams, emailOrNickname: e.target.value })}
          margin="normal"
        />
        <TextField
          label="Search"
          value={searchParams.search}
          onChange={(e) => setSearchParams({ ...searchParams, search: e.target.value })}
          margin="normal"
          onKeyPress={(e) => {
            if (e.key === 'Enter') {
              onSearch();
            }
          }}
        />
        <Button onClick={onSearch} variant="contained" color="primary">
          Search
        </Button>
      </Box>
      <TicketList tickets={tickets} onTicketClick={fetchTicketActivity} />
      <TicketModal open={modalOpen} onClose={() => setModalOpen(false)} ticket={selectedTicket} onViewMember={onViewMember} onViewReview={onViewReview} />
    </Box>
  );
};

export default Tickets;
