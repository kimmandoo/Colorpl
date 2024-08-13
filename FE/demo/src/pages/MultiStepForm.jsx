import React, { useState } from 'react';
import TheaterSearchPage from './TheaterSearchPage';
import ShowDetailPage from './ShowDetailPage';
import PriceBySeatClassPage from './PriceBySeatClassPage';
import SeatLayoutPage from './SeatLayoutPage';
import SchedulePage from './ShowSchedulePage';

const MultiStepForm = () => {
  const [step, setStep] = useState(1);
  const [selectedHallId, setSelectedHallId] = useState(null);
  const [showDetail, setShowDetail] = useState(null);
  const [priceDetails, setPriceDetails] = useState([]);
  const [seatLayout, setSeatLayout] = useState([]);
  const [schedule, setSchedule] = useState(null);

  const handleNextStep = () => {
    setStep(prevStep => prevStep + 1);
  };

  const handlePrevStep = () => {
    setStep(prevStep => prevStep - 1);
  };

  const handleTheaterSelect = (hallId) => {
    setSelectedHallId(hallId);
    handleNextStep();
  };

  const renderStep = () => {
    switch (step) {
      case 1:
        return <TheaterSearchPage onHallSelect={handleTheaterSelect} />;
      case 2:
        return <ShowDetailPage selectedHallId={selectedHallId} onShowDetailSubmit={(detail) => { setShowDetail(detail); handleNextStep(); }} />;
      case 3:
        return <PriceBySeatClassPage showDetail={showDetail} onPricesSubmit={(prices) => { setPriceDetails(prices); handleNextStep(); }} />;
      case 4:
        return <SeatLayoutPage showDetail={showDetail} onSeatLayoutSubmit={(layout) => { setSeatLayout(layout); handleNextStep(); }} />;
      case 5:
        return <SchedulePage showDetail={showDetail} onScheduleSubmit={(scheduleData) => { setSchedule(scheduleData); handleNextStep(); }} />;
      case 6:
        return (
          <div>
            <h2>Summary</h2>
            <p><strong>Selected Hall ID:</strong> {selectedHallId}</p>
            <p><strong>Show Details:</strong> {JSON.stringify(showDetail, null, 2)}</p>
            <p><strong>Price Details:</strong> {JSON.stringify(priceDetails, null, 2)}</p>
            <p><strong>Seat Layout:</strong> {JSON.stringify(seatLayout, null, 2)}</p>
            <p><strong>Schedule:</strong> {JSON.stringify(schedule, null, 2)}</p>
          </div>
        );
      default:
        return <div>All steps completed!</div>;
    }
  };

  return (
    <div>
      <h1>Multi-Step Form</h1>
      <div>
        {renderStep()}
      </div>
      <div>
        {step > 1 && step < 6 && <button onClick={handlePrevStep}>Back</button>}
      </div>
    </div>
  );
};

export default MultiStepForm;
