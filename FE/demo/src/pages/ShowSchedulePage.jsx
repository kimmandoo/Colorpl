import React, { useState } from 'react';
import api from '../api';

const SchedulePage = ({ showDetail, onScheduleSubmit }) => {
  const [schedule, setSchedule] = useState({
    show_schedule_date_time: new Date().toISOString().substring(0, 16),
    show_detail_id: showDetail.show_detail_id,
    runtime: showDetail.show_detail_runtime || '', // show_detail_runtime 값을 기본값으로 설정
  });

  const handleSubmit = async () => {
    try {
      const response = await api.post('/vm/schedules', schedule);
      onScheduleSubmit(response.data);
    } catch (error) {
      if (error.response && error.response.status === 400) {
        alert('Schedule conflicts with an existing one.');
      } else {
        console.error('Error setting schedule', error);
      }
    }
  };

  return (
    <div>
      <h2>Set Schedule</h2>
      <input
        type="datetime-local"
        value={schedule.show_schedule_date_time}
        onChange={(e) => setSchedule({ ...schedule, show_schedule_date_time: e.target.value })}
      />
      <input
        type="text"
        placeholder="Runtime (e.g., 2시간 5분)"
        value={schedule.runtime}
        onChange={(e) => setSchedule({ ...schedule, runtime: e.target.value })}
      />
      <button onClick={handleSubmit}>Submit Schedule</button>
    </div>
  );
};

export default SchedulePage;
