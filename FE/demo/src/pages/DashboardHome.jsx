import React from 'react';
import { Box, Paper, Typography } from '@mui/material';
import ShowDetailDashboard from './ShowDetailsDashboard'; // ShowDetailDashboard 임포트
import MemberActivityDashboard from './MemberActivityDashboard'; // MemberActivityDashboard 임포트

const DashboardHome = () => {
  return (
    <Box display="flex" flexWrap="wrap" justifyContent="center" p={2} gap={2} alignItems="center" height="100vh" overflow="auto">
      {/* MemberActivityDashboard 요약본 */}
      <Paper elevation={3} sx={{ width: 'calc(100% - 32px)', height: '400px', overflow: 'auto' }}>
        <Box p={2}>
          <Typography variant="h6" gutterBottom>

          </Typography>
          <MemberActivityDashboard />
        </Box>
      </Paper>
      {/* ShowDetailDashboard 요약본 */}
      <Paper elevation={3} sx={{ width: 'calc(100% - 32px)', height: '400px', overflow: 'auto' }}>
        <Box p={2}>
          <Typography variant="h6" gutterBottom>
          </Typography>
          <ShowDetailDashboard />
        </Box>
      </Paper>

    </Box>
  );
};

export default DashboardHome;
