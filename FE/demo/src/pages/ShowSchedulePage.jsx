import React, { useState, useEffect } from 'react';
import { Box, Button, TextField, Typography, Grid } from '@mui/material';
import { DatePicker, TimePicker, LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns'; // 필요한 날짜 어댑터
import api from '../api';

const SchedulePage = ({ showDetail, onScheduleSubmit, hallId }) => {
  const [schedules, setSchedules] = useState([
    {
      date: new Date(),
      time: new Date(),
      show_detail_id: showDetail.show_detail_id,
      runtime: showDetail.show_detail_runtime || '',
    },
  ]);
  const [existingSchedules, setExistingSchedules] = useState([]);

  useEffect(() => {
    console.log('Received hallId:', hallId); // Debugging: 확인을 위한 콘솔 로그
    if (hallId) {
      const fetchSchedules = async () => {
        try {
          const response = await api.get(`/vm/schedules/hall/${hallId}`);
          setExistingSchedules(response.data);
        } catch (error) {
          console.error('Error fetching schedules:', error);
        }
      };
      fetchSchedules();
    }
  }, [hallId]);

  const handleAddSchedule = () => {
    setSchedules([
      ...schedules,
      {
        date: new Date(),
        time: new Date(),
        show_detail_id: showDetail.show_detail_id,
        runtime: showDetail.show_detail_runtime || '',
      },
    ]);
  };

  const handleScheduleChange = (index, field, value) => {
    const updatedSchedules = schedules.map((schedule, i) =>
      i === index ? { ...schedule, [field]: value } : schedule
    );
    setSchedules(updatedSchedules);
  };

  const checkForConflict = (date, time) => {
    const selectedDateTime = new Date(date);
    selectedDateTime.setHours(time.getHours());
    selectedDateTime.setMinutes(time.getMinutes());

    return existingSchedules.some((schedule) => {
      const existingDateTime = new Date(schedule.show_schedule_date_time);
      return selectedDateTime.getTime() === existingDateTime.getTime();
    });
  };

  const handleSubmit = async () => {
    try {
      const responses = await Promise.all(
        schedules.map((schedule) => {
          const show_schedule_date_time = new Date(schedule.date);
          show_schedule_date_time.setHours(schedule.time.getHours());
          show_schedule_date_time.setMinutes(schedule.time.getMinutes());

          if (checkForConflict(schedule.date, schedule.time)) {
            alert('스케줄이 이미 존재합니다.');
            return null;
          }

          return api.post('/vm/schedules', {
            show_schedule_date_time: show_schedule_date_time.toISOString(),
            show_detail_id: schedule.show_detail_id,
          });
        })
      );
      onScheduleSubmit(responses.map((response) => response?.data).filter(Boolean));
    } catch (error) {
      if (error.response && error.response.status === 400) {
        alert('스케줄이 이미 존재합니다.');
      } else {
        console.error('Error setting schedules', error);
      }
    }
  };

  return (
    <LocalizationProvider dateAdapter={AdapterDateFns}>
      <Box
        display="flex"
        flexDirection="column"
        alignItems="center"
        justifyContent="center"
        minHeight="100vh"
        p={3}
      >
        <Typography variant="h4" gutterBottom>
          스케줄 설정
        </Typography>
        {hallId ? (
          <>
            {schedules.map((schedule, index) => (
              <Box key={index} mb={2} width="100%" maxWidth="600px">
                <Grid container spacing={2}>
                  <Grid item xs={6}>
                    <DatePicker
                      label="날짜 선택"
                      value={schedule.date}
                      onChange={(newDate) => handleScheduleChange(index, 'date', newDate)}
                      renderInput={(params) => <TextField {...params} fullWidth />}
                    />
                  </Grid>
                  <Grid item xs={6}>
                    <TimePicker
                      label="시간 선택"
                      value={schedule.time}
                      onChange={(newTime) => handleScheduleChange(index, 'time', newTime)}
                      renderInput={(params) => <TextField {...params} fullWidth />}
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <TextField
                      fullWidth
                      label="런타임 (예: 2시간 5분)"
                      value={schedule.runtime}
                      onChange={(e) => handleScheduleChange(index, 'runtime', e.target.value)}
                    />
                  </Grid>
                </Grid>
              </Box>
            ))}
            <Box display="flex" justifyContent="center" gap={2} mt={2}>
              <Button variant="contained" onClick={handleAddSchedule}>
                스케줄 추가
              </Button>
              <Button variant="outlined" onClick={handleSubmit}>
                모든 스케줄 제출
              </Button>
            </Box>
          </>
        ) : (
          <Typography variant="h6">홀 ID가 제공되지 않았습니다. 이전 단계로 돌아가 홀을 선택해주세요.</Typography>
        )}
      </Box>
    </LocalizationProvider>
  );
};

export default SchedulePage;
