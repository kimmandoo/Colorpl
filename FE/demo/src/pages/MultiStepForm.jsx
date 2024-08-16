import React, { useState } from 'react';
import { Box, Button, Typography, Stepper, Step, StepLabel, Grid, TextField } from '@mui/material';
import TheaterSearchPage from './TheaterSearchPage';
import ShowDetailPage from './ShowDetailPage';
import PriceBySeatClassPage from './PriceBySeatClassPage';
import SeatLayoutPage from './SeatLayoutPage';
import SchedulePage from './ShowSchedulePage';

const MultiStepForm = () => {
  const [step, setStep] = useState(0);
  const [formData, setFormData] = useState({
    selectedHallId: null,
    showDetail: null,
    priceDetails: [],
    seatLayout: [],
    schedule: null,
  });

  const steps = ['극장 선택', '공연 정보', '가격 설정', '좌석 배치', '스케줄 설정', '요약'];

  const handleNextStep = () => {
    setStep((prevStep) => prevStep + 1);
  };

  const handlePrevStep = () => {
    setStep((prevStep) => prevStep - 1);
  };

  const handleTheaterSelect = (hallId) => {
    console.log('Selected Hall ID:', hallId); // Debugging: 확인을 위한 콘솔 로그
    setFormData((prev) => ({ ...prev, selectedHallId: hallId }));
    handleNextStep();
  };

  const handleSubmit = () => {
    console.log('Data to submit:', formData);
    // 여기서 API 호출을 수행할 수 있습니다.
  };

  const handleReset = () => {
    setStep(0);
    setFormData({
      selectedHallId: null,
      showDetail: null,
      priceDetails: [],
      seatLayout: [],
      schedule: null,
    });
  };

  const renderStep = () => {
    switch (step) {
      case 0:
        return <TheaterSearchPage onHallSelect={(hallId) => { handleTheaterSelect(hallId); }} />;
      case 1:
        return <ShowDetailPage
          selectedHallId={formData.selectedHallId}
          onShowDetailSubmit={(detail) => {
            setFormData((prev) => ({ ...prev, showDetail: detail }));
            handleNextStep();
          }}
        />;
      case 2:
        return <PriceBySeatClassPage
          showDetail={formData.showDetail}
          onPricesSubmit={(prices) => {
            setFormData((prev) => ({ ...prev, priceDetails: prices }));
            handleNextStep();
          }}
        />;
      case 3:
        return <SeatLayoutPage
          showDetail={formData.showDetail}
          onSeatLayoutSubmit={(layout) => {
            setFormData((prev) => ({ ...prev, seatLayout: layout }));
            handleNextStep();
          }}
        />;
      case 4:
        return <SchedulePage
          showDetail={formData.showDetail}
          onScheduleSubmit={(scheduleData) => {
            setFormData((prev) => ({ ...prev, schedule: scheduleData }));
            handleNextStep();
          }}
          hallId={formData.selectedHallId} // hallId 전달
        />;
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
                    value={formData.selectedHallId || ''}
                    InputProps={{ readOnly: true }}
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    fullWidth
                    label="공연 이름"
                    value={formData.showDetail?.show_detail_name || ''}
                    InputProps={{ readOnly: true }}
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    fullWidth
                    label="가격 정보"
                    value={(formData.priceDetails ?? []).map(price => `${price.price_by_seat_class_seat_class}: ${price.price_by_seat_class_price}`).join(', ')}
                    InputProps={{ readOnly: true }}
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    fullWidth
                    label="좌석 배치"
                    value={(formData.seatLayout ?? []).map(layout => `Row: ${layout.row}, Col: ${layout.col}, Class: ${layout.class}`).join(', ')}
                    InputProps={{ readOnly: true }}
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    fullWidth
                    label="스케줄"
                    value={(formData.schedule ?? []).map(s => `${s.show_schedule_date_time}`).join(', ') || ''}
                    InputProps={{ readOnly: true }}
                  />
                </Grid>
              </Grid>
            </form>
            {/* <Button variant="contained" color="primary" onClick={handleSubmit} sx={{ mt: 2 }}>
              제출하기
            </Button> */}
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
