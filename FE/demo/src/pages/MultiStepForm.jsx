import React, { useState } from 'react';
import { Box, Button, Typography, Stepper, Step, StepLabel, Grid, TextField } from '@mui/material';
import TheaterSearchPage from './TheaterSearchPage';
import ShowDetailPage from './ShowDetailPage';
import PriceBySeatClassPage from './PriceBySeatClassPage';
import SeatLayoutPage from './SeatLayoutPage';
import SchedulePage from './ShowSchedulePage';

const MultiStepForm = () => {
  const [step, setStep] = useState(0);
  const [selectedHallId, setSelectedHallId] = useState(null);
  const [showDetail, setShowDetail] = useState(null);
  const [priceDetails, setPriceDetails] = useState([]);
  const [seatLayout, setSeatLayout] = useState([]);
  const [schedule, setSchedule] = useState(null);

  const steps = ['극장 선택', '공연 정보', '가격 설정', '좌석 배치', '스케줄 설정', '요약'];

  const handleNextStep = () => {
    setStep((prevStep) => prevStep + 1);
  };

  const handlePrevStep = () => {
    setStep((prevStep) => prevStep - 1);
  };

  const handleTheaterSelect = (hallId) => {
    setSelectedHallId(hallId);
    handleNextStep();
  };

  const handleReset = () => {
    setStep(0);
    setSelectedHallId(null);
    setShowDetail(null);
    setPriceDetails([]);
    setSeatLayout([]);
    setSchedule(null);
  };

  const renderStep = () => {
    switch (step) {
      case 0:
        return <TheaterSearchPage onHallSelect={handleTheaterSelect} />;
      case 1:
        return <ShowDetailPage selectedHallId={selectedHallId} onShowDetailSubmit={(detail) => { setShowDetail(detail); handleNextStep(); }} />;
      case 2:
        return <PriceBySeatClassPage showDetail={showDetail} onPricesSubmit={(prices) => { setPriceDetails(prices); handleNextStep(); }} />;
      case 3:
        return <SeatLayoutPage showDetail={showDetail} onSeatLayoutSubmit={(layout) => { setSeatLayout(layout); handleNextStep(); }} />;
      case 4:
        return <SchedulePage showDetail={showDetail} onScheduleSubmit={(scheduleData) => { setSchedule(scheduleData); handleNextStep(); }} />;
      case 5:
        return (
          <Box textAlign="center">
            <Typography variant="h4" gutterBottom>
              공연 등록 완료
            </Typography>
            <form>
              <Grid container spacing={2}>
                <Grid item xs={12}>
                  <TextField
                    fullWidth
                    label="선택된 홀 ID"
                    value={selectedHallId || ''}
                    InputProps={{
                      readOnly: true,
                    }}
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    fullWidth
                    label="공연 이름"
                    value={showDetail?.show_detail_name || ''}
                    InputProps={{
                      readOnly: true,
                    }}
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    fullWidth
                    label="가격 정보"
                    value={priceDetails.map(price => `${price.price_by_seat_class_seat_class}: ${price.price_by_seat_class_price}`).join(', ')}
                    InputProps={{
                      readOnly: true,
                    }}
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    fullWidth
                    label="좌석 배치"
                    value={seatLayout.map(layout => `Row: ${layout.row}, Col: ${layout.col}, Class: ${layout.class}`).join(', ')}
                    InputProps={{
                      readOnly: true,
                    }}
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    fullWidth
                    label="스케줄"
                    value={schedule?.map(s => `${s.show_schedule_date_time}`).join(', ') || ''}
                    InputProps={{
                      readOnly: true,
                    }}
                  />
                </Grid>
              </Grid>
            </form>
            <Button variant="contained" color="primary" onClick={handleReset} sx={{ mt: 2 }}>
              다시 등록하기
            </Button>
          </Box>
        );
      default:
        return <Typography variant="h6">모든 단계가 완료되었습니다!</Typography>;
    }
  };

  return (
    <Box sx={{ width: '80%', margin: '0 auto', paddingTop: 4 }}>
      <Typography variant="h3" textAlign="center" gutterBottom>
        공연 등록
      </Typography>
      <Stepper activeStep={step} alternativeLabel>
        {steps.map((label) => (
          <Step key={label}>
            <StepLabel>{label}</StepLabel>
          </Step>
        ))}
      </Stepper>
      <Box sx={{ marginTop: 4 }}>
        {renderStep()}
      </Box>
      <Grid container justifyContent="space-between" sx={{ marginTop: 2 }}>
        {step > 0 && step < 5 && (
          <Button variant="outlined" onClick={handlePrevStep}>
            이전
          </Button>
        )}
        {step < 5 && (
          <Button variant="contained" color="primary" onClick={handleNextStep}>
            다음
          </Button>
        )}
      </Grid>
    </Box>
  );
};

export default MultiStepForm;
