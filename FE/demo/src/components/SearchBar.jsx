// components/SearchBar.jsx
import React, { useState } from 'react';
import { Box, TextField, Button } from '@mui/material';

const SearchBar = ({ onSearch, fields }) => {
  const [searchParams, setSearchParams] = useState({});

  const handleInputChange = (field) => (event) => {
    setSearchParams({
      ...searchParams,
      [field]: event.target.value,
    });
  };

  const handleSearch = () => {
    onSearch(searchParams);
  };

  return (
    <Box display="flex" gap={2}>
      {fields.map((field) => (
        <TextField
          key={field}
          label={field}
          variant="outlined"
          size="small"
          value={searchParams[field] || ''}
          onChange={handleInputChange(field)}
        />
      ))}
      <Button variant="contained" color="primary" onClick={handleSearch}>
        Search
      </Button>
    </Box>
  );
};

export default SearchBar;
