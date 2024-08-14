import React, { useState } from 'react';
import api from '../api';

const PriceBySeatClassPage = ({ showDetail, onPricesSubmit }) => {
  const [prices, setPrices] = useState([{ price_by_seat_class_seat_class: 1, price_by_seat_class_price: 10000 }]); // 'A' 대신 1로 설정

  const handleAddPrice = () => {
    setPrices([...prices, { price_by_seat_class_seat_class: 0, price_by_seat_class_price: 0 }]); // 기본값을 0으로 설정
  };

  const handleSeatClassChange = (index, value) => {
    const updatedPrices = prices.map((price, i) =>
      i === index ? { ...price, price_by_seat_class_seat_class: Number(value) } : price
    );
    setPrices(updatedPrices);
  };

  const handlePriceChange = (index, value) => {
    const updatedPrices = prices.map((price, i) =>
      i === index ? { ...price, price_by_seat_class_price: value } : price
    );
    setPrices(updatedPrices);
  };

  const handleSubmit = async () => {
    try {
      for (const price of prices) {
        await api.post('/vm/price', {
          show_detail_id: showDetail.show_detail_id,
          ...price,
        });
      }
      onPricesSubmit();
    } catch (error) {
      console.error('Error setting prices', error);
    }
  };

  return (
    <div>
      <h2>Set Price by Seat Class</h2>
      {prices.map((price, index) => (
        <div key={index}>
          <select
            value={price.price_by_seat_class_seat_class}
            onChange={(e) => handleSeatClassChange(index, e.target.value)}
          >
            <option value={-1}>Empty</option>
            <option value={0}>B</option>
            <option value={1}>A</option>
            <option value={2}>S</option>
            <option value={3}>R</option>
          </select>
          <input
            type="number"
            value={price.price_by_seat_class_price}
            onChange={(e) => handlePriceChange(index, e.target.value)}
          />
        </div>
      ))}
      <button onClick={handleAddPrice}>Add Price</button>
      <button onClick={handleSubmit}>Submit Prices</button>
    </div>
  );
};

export default PriceBySeatClassPage;
