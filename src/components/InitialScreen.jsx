import React from 'react';
import { Box, Center, Image } from '@chakra-ui/react';
import { useNavigate } from 'react-router-dom';

const InitialScreen = () => {
  const navigate = useNavigate();

  return (
    <Center height="100vh">
      <Box width="500px" height="500px"> {/* 로그인 폼과 같은 크기 */}
        <Image 
          src={`${process.env.PUBLIC_URL}/assets/Colorpl.png`} 
          alt="Colorpl"
          onClick={() => navigate('/login')} 
          cursor="pointer"
          boxSize="500px" // 이미지 크기 조정
          objectFit="cover"
        />
      </Box>
    </Center>
  );
};

export default InitialScreen;
