import React, { useState } from 'react';
import api from '../api';

const ShowDetailPage = ({ onShowDetailSubmit, selectedHallId }) => {
  const [showDetail, setShowDetail] = useState({
    show_detail_id: null,
    SHOW_API_ID: '',
    show_detail_area: '',
    show_detail_cast: '',
    show_detail_category: '',
    show_detail_name: '',
    show_detail_poster_image_path: '',
    show_detail_runtime: '',
    show_detail_state: '',
    hall_id: selectedHallId || null, // Use selectedHallId if provided
  });

  const [manualJsonInput, setManualJsonInput] = useState('');
  const [useManualInput, setUseManualInput] = useState(false);

  const handleChange = (e) => {
    setShowDetail({
      ...showDetail,
      [e.target.name]: e.target.value,
    });
  };

  const handleManualInputChange = (e) => {
    setManualJsonInput(e.target.value);
  };

  const handleSubmit = async () => {
    try {
      const dataToSubmit = useManualInput ? JSON.parse(manualJsonInput) : showDetail;
      const response = await api.post('/vm/shows', dataToSubmit);
      onShowDetailSubmit(response.data);
    } catch (error) {
      console.error('Error creating show detail', error);
      alert('Invalid JSON input.');
    }
  };

  const toggleManualInput = () => {
    setUseManualInput(!useManualInput);
  };

  return (
    <div>
      <h2>Create Show Detail</h2>
      <div>
        <label>
          <input type="checkbox" checked={useManualInput} onChange={toggleManualInput} />
          Use Manual JSON Input
        </label>
      </div>

      {useManualInput ? (
        <div>
          <textarea
            rows="10"
            cols="50"
            placeholder={`{
  "show_detail_api_id": "PF236460",
  "show_detail_area": "서울특별시",
  "show_detail_cast": "Actor Name",
  "show_detail_category": "PERFORMANCE",
  "show_detail_name": "Show Name",
  "show_detail_poster_image_path": "http://example.com/poster.jpg",
  "show_detail_runtime": "2시간",
  "show_detail_state": "SHOWING",
  "hall_id": ${selectedHallId || null}
}`}
            value={manualJsonInput}
            onChange={handleManualInputChange}
          />
        </div>
      ) : (
        <form>
          <div>
            <label>Show API ID:</label>
            <input type="text" name="SHOW_API_ID" value={showDetail.SHOW_API_ID} onChange={handleChange} />
          </div>
          <div>
            <label>Show Area:</label>
            <select name="show_detail_area" value={showDetail.show_detail_area} onChange={handleChange}>
              <option value="">Select Area</option>
              <option value="서울특별시">서울특별시</option>
              <option value="부산광역시">부산광역시</option>
              <option value="대구광역시">대구광역시</option>
              <option value="인천광역시">인천광역시</option>
              <option value="광주광역시">광주광역시</option>
              <option value="대전광역시">대전광역시</option>
              <option value="울산광역시">울산광역시</option>
              <option value="세종특별자치시">세종특별자치시</option>
              <option value="경기도">경기도</option>
              <option value="강원특별자치도">강원특별자치도</option>
              <option value="충청북도">충청북도</option>
              <option value="충청남도">충청남도</option>
              <option value="전라북도">전라북도</option>
              <option value="전라남도">전라남도</option>
              <option value="경상북도">경상북도</option>
              <option value="경상남도">경상남도</option>
              <option value="제주특별자치도">제주특별자치도</option>
              <option value="해외 기타 지역">해외 기타 지역</option>
            </select>
          </div>
          <div>
            <label>Show Cast:</label>
            <input type="text" name="show_detail_cast" value={showDetail.show_detail_cast} onChange={handleChange} />
          </div>
          <div>
            <label>Show Genre:</label>
            <select name="show_detail_category" value={showDetail.show_detail_category} onChange={handleChange}>
              <option value="">Select Genre</option>
              <option value="PLAY">연극</option>
              <option value="MOVIE">영화</option>
              <option value="PERFORMANCE">공연</option>
              <option value="CONCERT">콘서트</option>
              <option value="MUSICAL">뮤지컬</option>
              <option value="EXHIBITION">전시회</option>
              <option value="ETC">기타</option>
            </select>
          </div>
          <div>
            <label>Show Name:</label>
            <input type="text" name="show_detail_name" value={showDetail.show_detail_name} onChange={handleChange} />
          </div>
          <div>
            <label>Show Poster URL:</label>
            <input type="text" name="show_detail_poster_image_path" value={showDetail.show_detail_poster_image_path} onChange={handleChange} />
          </div>
          <div>
            <label>Show Runtime:</label>
            <input type="text" name="show_detail_runtime" value={showDetail.show_detail_runtime} onChange={handleChange} />
          </div>
          <div>
            <label>Show State:</label>
            <select name="show_detail_state" value={showDetail.show_detail_state} onChange={handleChange}>
              <option value="">Select State</option>
              <option value="COMPLETED">완료</option>
              <option value="SCHEDULED">예정</option>
              <option value="SHOWING">공연 중</option>
            </select>
          </div>
          <div>
            <label>Hall ID:</label>
            <input type="number" name="hall_id" value={showDetail.hall_id || ''} onChange={handleChange} />
          </div>
        </form>
      )}

      <button onClick={handleSubmit}>Create Show Detail</button>
    </div>
  );
};

export default ShowDetailPage;
