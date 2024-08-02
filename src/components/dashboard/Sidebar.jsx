import React, { useEffect, useState } from 'react';
import { Box, VStack, HStack, Icon, Text, Button } from '@chakra-ui/react';
import { FaBars, FaHome, FaUser, FaCog } from 'react-icons/fa';
// import { Link } from 'react-router-dom'; // 주석 처리

const sidebarItems = {
  cs: [
    { name: '이슈', path: '/cs/issues' },
    { name: '사용자 관리', path: '/cs/user-management' },
    { name: '리뷰 관리', path: '/cs/review-management' },
    { name: '댓글 관리', path: '/cs/comment-management' },
    { name: '문의 내역', path: '/cs/customer' },
  ],
  sell: [
    { name: '판매 현황', path: '/sell/sales' },
    { name: '티켓 등록', path: '/sell/ticket' },
    { name: '좌석 등록', path: '/sell/seat' },
  ],
  dev: [
    { name: 'Loki', path: '/dev/loki' },
    { name: 'Log', path: '/dev/log' },
    { name: 'Prometheus', path: '/dev/prometheus' },
  ],
};

const Sidebar = ({ isOpen, onToggle, selectedTab }) => {
  const [selectedItem, setSelectedItem] = useState(sidebarItems[selectedTab][0].name);

  const handleItemClick = (item) => {
    setSelectedItem(item.name);
  };

  const getItemBg = (item) => {
    return selectedItem === item.name ? "white" : "transparent";
  };

  const getItemColor = (item) => {
    return selectedItem === item.name ? "gray.800" : "white";
  };

  useEffect(() => {
    setSelectedItem(sidebarItems[selectedTab][0].name);
  }, [selectedTab]);

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
            <Box
              // as={Link}
              // to={item.path}
              key={item.name}
              onClick={() => handleItemClick(item)}
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
          ))}
        </VStack>
      )}
      <Box height="30%" display="flex" alignItems="center" justifyContent="center">
        <HStack spacing={8}>
          {/* <Link to="/cs/issues" onClick={() => handleItemClick(sidebarItems.cs[0])}> */}
            <Icon as={FaHome} boxSize={6} cursor="pointer" onClick={() => handleItemClick(sidebarItems.cs[0])} />
          {/* </Link> */}
          <Icon as={FaUser} boxSize={6} />
          <Icon as={FaCog} boxSize={6} />
        </HStack>
      </Box>
    </Box>
  );
};

export default Sidebar;
