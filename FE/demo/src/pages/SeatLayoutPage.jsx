import React, { useState } from 'react';
import api from '../api';

const seatColors = {
  0: '#CCCCCC', // Empty (0) - Grey
  1: '#FFFF00', // B (1) - Yellow
  2: '#0000FF', // A (2) - Blue
  3: '#FF0000', // R (3) - Red
};

const SeatLayoutPage = ({ showDetail, onSeatLayoutSubmit, priceBySeatClass }) => {
  const [seatLayout, setSeatLayout] = useState([...Array(160).fill(0)]);

  const handleSeatClick = (index) => {
    const newLayout = [...seatLayout];
    newLayout[index] = (newLayout[index] + 1) % 4; // 0~3 순환
    setSeatLayout(newLayout);
  };

  const handleSubmit = async () => {
    try {
      const seats = seatLayout.map((seat_class, index) => ({
        seat_col: index % 16,
        seat_row: Math.floor(index / 16),
        seat_class: seat_class, // 여기서 seat_class는 이미 정수 값으로 되어 있어야 합니다.
        show_detail_id: showDetail.show_detail_id,
      }));
      await api.post('/vm/seats', { seats });
      onSeatLayoutSubmit();
    } catch (error) {
      console.error('Error setting seat layout', error);
    }
  };

  return (
    <div>
      <h2>Set Seat Layout</h2>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(16, 20px)' }}>
        {seatLayout.map((seat, index) => (
          <div
            key={index}
            onClick={() => handleSeatClick(index)}
            style={{
              width: '20px',
              height: '20px',
              backgroundColor: seatColors[seat],
              border: '1px solid black',
              cursor: 'pointer',
            }}
          ></div>
        ))}
      </div>
      <button onClick={handleSubmit}>Submit Seat Layout</button>
    </div>
  );
};

export default SeatLayoutPage;
