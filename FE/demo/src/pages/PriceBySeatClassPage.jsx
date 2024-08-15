import React, { useState } from 'react';
import api from '../api';

const PriceBySeatClassPage = ({ showDetail, onPricesSubmit }) => {
  const [prices, setPrices] = useState([{ price_by_seat_class_seat_class: 1, price_by_seat_class_price: 10000 }]); 

  const handleAddPrice = () => {
    if (prices.length < 5) {
      setPrices([...prices, { price_by_seat_class_seat_class: 0, price_by_seat_class_price: 0 }]);
    } else {
      alert('최대 5개의 가격만 설정할 수 있습니다.');
    }
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

  const handleRemovePrice = (index) => {
    const updatedPrices = prices.filter((_, i) => i !== index);
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
      console.error('가격 설정 중 오류 발생', error);
    }
  };

  return (
    <div style={styles.container}>
      <h2 style={styles.heading}>좌석 등급별 가격 설정</h2>
      {prices.map((price, index) => (
        <div key={index} style={styles.priceRow}>
          <select
            value={price.price_by_seat_class_seat_class}
            onChange={(e) => handleSeatClassChange(index, e.target.value)}
            style={styles.select}
          >
            <option value={-1}>비어 있음</option>
            <option value={0}>B</option>
            <option value={1}>A</option>
            <option value={2}>S</option>
            <option value={3}>R</option>
          </select>
          <input
            type="number"
            value={price.price_by_seat_class_price}
            onChange={(e) => handlePriceChange(index, e.target.value)}
            style={styles.input}
          />
          <button onClick={() => handleRemovePrice(index)} style={styles.removeButton}>
            삭제
          </button>
        </div>
      ))}
      <div style={styles.buttonContainer}>
        <button onClick={handleAddPrice} style={styles.button} disabled={prices.length >= 5}>
          가격 추가
        </button>
        <button onClick={handleSubmit} style={styles.button}>
          가격 제출
        </button>
      </div>
    </div>
  );
};

const styles = {
  container: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    minHeight: '100vh',
    backgroundColor: '#f9f9f9',
    padding: '20px',
  },
  heading: {
    marginBottom: '20px',
    color: '#333',
  },
  priceRow: {
    display: 'flex',
    alignItems: 'center',
    marginBottom: '10px',
  },
  select: {
    marginRight: '10px',
    padding: '8px',
    fontSize: '16px',
  },
  input: {
    padding: '8px',
    fontSize: '16px',
    width: '100px',
  },
  removeButton: {
    marginLeft: '10px',
    padding: '8px',
    fontSize: '16px',
    backgroundColor: '#ff4d4d',
    color: '#fff',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
  },
  buttonContainer: {
    marginTop: '20px',
    display: 'flex',
    gap: '10px',
  },
  button: {
    padding: '10px 20px',
    fontSize: '16px',
    cursor: 'pointer',
    backgroundColor: '#007bff',
    color: '#fff',
    border: 'none',
    borderRadius: '5px',
    transition: 'background-color 0.3s ease',
  },
};

export default PriceBySeatClassPage;
