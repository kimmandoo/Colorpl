import React, { useEffect, useState } from 'react';
import { Box, VStack, HStack, Icon, Text, Button } from '@chakra-ui/react';
import { FaBars, FaHome, FaUser, FaCog } from 'react-icons/fa';
import { Link, useLocation } from 'react-router-dom';

const sidebarItems = {
  cs: [
    { name: '이슈', path: '/dashboard/cs/issues' },
    { name: '사용자 관리', path: '/dashboard/cs/user-management' },
    { name: '리뷰 관리', path: '/dashboard/cs/review-management' },
    { name: '댓글 관리', path: '/dashboard/cs/comment-management' },
    { name: '문의 내역', path: '/dashboard/cs/customer' },
  ],
  sell: [
    { name: '판매 현황', path: '/dashboard/sell/sales' },
    { name: '티켓 등록', path: '/dashboard/sell/ticket' },
    { name: '좌석 등록', path: '/dashboard/sell/seat' },
  ],
  dev: [
    { name: 'Loki', path: '/dashboard/dev/loki' },
    { name: 'Log', path: '/dashboard/dev/log' },
    { name: 'Prometheus', path: '/dashboard/dev/prometheus' },
  ],
};

const Sidebar = ({ isOpen, onToggle, selectedTab }) => {
  const location = useLocation();
  const [selectedItem, setSelectedItem] = useState(sidebarItems[selectedTab][0].name);

  useEffect(() => {
    const currentPath = location.pathname;
    const currentItem = sidebarItems[selectedTab].find(item => item.path === currentPath);
    if (currentItem) {
      setSelectedItem(currentItem.name);
    }
  }, [location, selectedTab]);

  const handleItemClick = (item) => {
    setSelectedItem(item.name);
  };

  const getItemBg = (item) => {
    return selectedItem === item.name ? "white" : "transparent";
  };

  const getItemColor = (item) => {
    return selectedItem === item.name ? "gray.800" : "white";
  };

  return (
    <Box
      position="fixed"
      left="0"
      top="0"
      width={isOpen ? "15%" : "5%"}
      height="100vh"
      bg="gray.800"
      color="white"
      p={isOpen ? 4 : 2}
      transition="width 0.3s ease-in-out"
      overflow="hidden"
    >
      <Button
        onClick={onToggle}
        position="absolute"
        top="50%"
        right={isOpen ? "-10px" : "-10px"}
        transform="translateY(-50%)"
        bg="transparent"
        _hover={{ bg: "transparent" }}
        color="white"
        borderRadius="none"
        padding={0}
        height="auto"
        minWidth="auto"
      >
        <Icon as={FaBars} boxSize={6} />
      </Button>
      {isOpen && (
        <VStack align="stretch" spacing={4} height="70%">
          <Text fontSize="2xl" fontWeight="bold" mb={4}>
            {selectedTab.toUpperCase()}
          </Text>
          {sidebarItems[selectedTab].map((item) => (
            <Link to={item.path} key={item.name} onClick={() => handleItemClick(item)}>
              <Box
                bg={getItemBg(item)}
                color={getItemColor(item)}
                p={2}
                cursor="pointer"
                borderRadius="md"
              >
                <Text fontSize="xl" mb={2}>
                  {item.name}
                </Text>
              </Box>
            </Link>
          ))}
        </VStack>
      )}
      <Box height="30%" display="flex" alignItems="center" justifyContent="center">
        <HStack spacing={8}>
          <Link to="/dashboard/cs/issues" onClick={() => handleItemClick(sidebarItems.cs[0])}>
            <Icon as={FaHome} boxSize={6} cursor="pointer" />
          </Link>
          <Link to="/dashboard/cs/user-management" onClick={() => handleItemClick(sidebarItems.cs[1])}>
            <Icon as={FaUser} boxSize={6} cursor="pointer" />
          </Link>
          <Link to="/dashboard/settings" onClick={() => handleItemClick(sidebarItems.dev[2])}>
            <Icon as={FaCog} boxSize={6} cursor="pointer" />
          </Link>
        </HStack>
      </Box>
    </Box>
  );
};

export default Sidebar;
