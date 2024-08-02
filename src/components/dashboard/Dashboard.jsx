import React, { useState } from 'react';
import { Box, Text, Center } from '@chakra-ui/react';
import { Route, Routes } from 'react-router-dom';
import Header from '../Header';
import Sidebar from './Sidebar';
import Issues from './Issues';
import UserManagement from './UserManagement';
import ReviewManagement from './ReviewManagement';
import CommentManagement from './CommentManagement';
import CustomerInquiries from './CustomerInquiries';

const Dashboard = () => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
  const [selectedTab, setSelectedTab] = useState('cs');

  const handleSidebarToggle = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };

  return (
    <>
      <Header />
      <Box display="flex" mt="64px">
        <Sidebar isOpen={isSidebarOpen} onToggle={handleSidebarToggle} selectedTab={selectedTab} />
        <Box flex="1" ml={isSidebarOpen ? "15%" : "5%"} transition="margin-left 0.3s ease-in-out">
          <Center flexDirection="column">
            <Routes>
              <Route path="issues" element={<Issues />} />
              <Route path="user-management" element={<UserManagement />} />
              <Route path="review-management" element={<ReviewManagement />} />
              <Route path="comment-management" element={<CommentManagement />} />
              <Route path="customer-inquiries" element={<CustomerInquiries />} />
              <Route path="/" element={<Text fontSize="2xl">대시보드 홈</Text>} />
            </Routes>
          </Center>
        </Box>
      </Box>
    </>
  );
};

export default Dashboard;
