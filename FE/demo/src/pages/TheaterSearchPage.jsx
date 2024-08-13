import React, { useState } from 'react';
import api from '../api';

const TheaterSearchPage = ({ onHallSelect }) => {
  const [theaterName, setTheaterName] = useState('');
  const [theaters, setTheaters] = useState([]);
  const [halls, setHalls] = useState([]);

  const searchTheaters = async () => {
    try {
      const response = await api.post('/vm/theaters/search', { theater_name: theaterName });
      setTheaters(response.data);
    } catch (error) {
      console.error('Error searching theaters', error);
    }
  };

  const handleTheaterClick = async (theater) => {
    try {
      const response = await api.get(`/vm/theaters/${theater.theater_id}/halls`);
      setHalls(response.data);
    } catch (error) {
      console.error('Error fetching halls', error);
    }
  };

  const handleHallClick = (hall) => {
    onHallSelect(hall.hall_id);
  };

  return (
    <div>
      <h2>극장 찾기</h2>
      <input 
        type="text" 
        value={theaterName} 
        onChange={(e) => setTheaterName(e.target.value)} 
        placeholder="Enter theater name" 
      />
      <button onClick={searchTheaters}>Search</button>

      <ul>
        {theaters.map(theater => (
          <li key={theater.theater_id} onClick={() => handleTheaterClick(theater)}>
            {theater.theater_name}
          </li>
        ))}
      </ul>

      {halls.length > 0 && (
        <div>
          <h3>홀 선택</h3>
          <ul>
            {halls.map(hall => (
              <li key={hall.hall_id} onClick={() => handleHallClick(hall)}>
                {hall.HALL_NAME}
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};

export default TheaterSearchPage;
