import React, { useState, useEffect } from 'react';
import api from '../api';

const seatColors = {
  0: '#CCCCCC', // 비어 있음 - 회색
  1: '#FFFF00', // B - 노랑
  2: '#0000FF', // A - 파랑
  3: '#FF0000', // R - 빨강
};

const SeatLayoutPage = ({ showDetail, onSeatLayoutSubmit }) => {
  const [seatLayout, setSeatLayout] = useState([...Array(160).fill(0)]);
  const [isDragging, setIsDragging] = useState(false);
  const [selectedSeatClass, setSelectedSeatClass] = useState(0);
  const [priceBySeatClass, setPriceBySeatClass] = useState({});

  useEffect(() => {
    const fetchPrices = async () => {
      try {
        const response = await api.get(`/vm/price/${showDetail.show_detail_id}`);
        setPriceBySeatClass(response.data);
      } catch (error) {
        console.error('가격 정보를 불러오는 데 실패했습니다.', error);
      }
    };

    fetchPrices();
  }, [showDetail.show_detail_id]);

  const handleSeatClick = (index) => {
    const newLayout = [...seatLayout];
    newLayout[index] = selectedSeatClass;
    setSeatLayout(newLayout);
  };

  const handleMouseDown = (index) => {
    setIsDragging(true);
    handleSeatClick(index);
  };

  const handleMouseUp = () => {
    setIsDragging(false);
  };

  const handleMouseEnter = (index) => {
    if (isDragging) {
      handleSeatClick(index);
    }
  };

  const handleSeatClassChange = (seatClass) => {
    setSelectedSeatClass(seatClass);
  };

  const handleSubmit = async () => {
    try {
      const seats = seatLayout.map((seat_class, index) => ({
        seat_col: index % 16,
        seat_row: Math.floor(index / 16),
        seat_class: seat_class,
        show_detail_id: showDetail.show_detail_id,
      }));
      await api.post('/vm/seats', { seats });
      onSeatLayoutSubmit();
    } catch (error) {
      console.error('좌석 레이아웃 설정 중 오류 발생', error);
    }
  };

  return (
    <div style={styles.container}>
      <h2 style={styles.heading}>좌석 배치 설정</h2>
      <div style={styles.priceInfoContainer}>
        {Object.keys(priceBySeatClass).map((seatClass) => (
          <div
            key={seatClass}
            style={{
              ...styles.seatClass,
              backgroundColor: seatColors[seatClass],
            }}
            onClick={() => handleSeatClassChange(Number(seatClass))}
          >
            {`등급: ${seatClass}, 가격: ${priceBySeatClass[seatClass]}원`}
          </div>
        ))}
      </div>
      <div
        style={styles.seatGrid}
        onMouseLeave={handleMouseUp} // 드래그 상태에서 마우스가 격자 바깥으로 나갔을 때 드래그 해제
      >
        {seatLayout.map((seat, index) => (
          <div
            key={index}
            onMouseDown={() => handleMouseDown(index)}
            onMouseUp={handleMouseUp}
            onMouseEnter={() => handleMouseEnter(index)}
            style={{
              ...styles.seat,
              backgroundColor: seatColors[seat],
            }}
          ></div>
        ))}
      </div>
      <button onClick={handleSubmit} style={styles.submitButton}>
        좌석 배치 제출
      </button>
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
    backgroundColor: '#f0f0f0',
    padding: '20px',
  },
  heading: {
    marginBottom: '20px',
    fontSize: '24px',
    color: '#333',
  },
  priceInfoContainer: {
    display: 'flex',
    gap: '10px',
    marginBottom: '20px',
  },
  seatClass: {
    padding: '10px 20px',
    cursor: 'pointer',
    color: '#fff',
    borderRadius: '5px',
  },
  seatGrid: {
    display: 'grid',
    gridTemplateColumns: 'repeat(16, 30px)',
    gap: '5px',
    marginBottom: '20px',
  },
  seat: {
    width: '30px',
    height: '30px',
    border: '1px solid black',
    cursor: 'pointer',
  },
  submitButton: {
    padding: '10px 20px',
    fontSize: '16px',
    backgroundColor: '#007bff',
    color: '#fff',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
  },
};

export default SeatLayoutPage;
