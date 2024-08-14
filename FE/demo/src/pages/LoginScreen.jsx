// pages/LoginScreen.jsx
import React, { useState } from 'react';
import { Box, Button, FormControl, FormLabel, Input, Text, VStack, HStack, SlideFade, Alert, AlertIcon, useToast } from '@chakra-ui/react';
import { useNavigate } from 'react-router-dom';
import api from '../api';

const LoginScreen = () => {
  const [isRegistering, setIsRegistering] = useState(false);
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const navigate = useNavigate();
  const toast = useToast();

  const resetForm = () => {
    setUsername('');
    setEmail('');
    setPassword('');
  };

  const handleLogin = async (event) => {
    event.preventDefault();
    try {
      const response = await api.post('/auth/token', new URLSearchParams({
        username,
        password
      }), {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      });
      console.log(response.data);
      localStorage.setItem('token', response.data.access_token);
      toast({
        description: "로그인 성공",
        status: 'success',
        duration: 5000,
        isClosable: true,
        position: 'top'
      });
      navigate('/dashboard'); // 로그인 후 대시보드로 리다이렉트
    } catch (error) {
      const errorMsg = error.response?.data?.detail || '로그인 실패';
      setMessage(typeof errorMsg === 'string' ? errorMsg : JSON.stringify(errorMsg));
      resetForm();
    }
  };

  const handleSignup = async (event) => {
    event.preventDefault();
    try {
      const response = await api.post('/auth/admin/signup', {
        username,
        email,
        password
      });
      console.log(response.data);
      setMessage(response.data.message);
      resetForm();
    } catch (error) {
      const errorMsg = error.response?.data?.detail || '회원가입 실패';
      setMessage(typeof errorMsg === 'string' ? errorMsg : JSON.stringify(errorMsg));
      resetForm();
    }
  };

  const handleToggle = () => {
    setIsRegistering(!isRegistering);
    setMessage(''); // 상태 변경 시 메시지 초기화
  };

  return (
    <Box height="100vh" display="flex" alignItems="center" justifyContent="center" overflow="auto">
      <SlideFade in={true} offsetY="20px">
        <Box 
          p={8} 
          borderWidth={1} 
          borderRadius="lg" 
          boxShadow="lg"
          width="400px"
          height="auto"
        >
          <form onSubmit={isRegistering ? handleSignup : handleLogin}>
            <VStack spacing={4} height="100%">
              <Text fontSize="3xl" fontFamily="cursive" mb={4}>Colorpl</Text>
              {message && (
                <Alert status="error" mb={4}>
                  <AlertIcon />
                  {message}
                </Alert>
              )}
              {isRegistering && (
                <>
                  <FormControl>
                    <FormLabel>이름</FormLabel>
                    <Input 
                      placeholder="이름" 
                      value={username}
                      onChange={(e) => setUsername(e.target.value)}
                    />
                  </FormControl>
                  <FormControl>
                    <FormLabel>이메일</FormLabel>
                    <Input 
                      type="email" 
                      placeholder="이메일" 
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                    />
                  </FormControl>
                </>
              )}
              {!isRegistering && (
                <FormControl>
                  <FormLabel>이름</FormLabel>
                  <Input 
                    placeholder="이름" 
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                  />
                </FormControl>
              )}
              <FormControl>
                <FormLabel>비밀번호</FormLabel>
                <Input 
                  type="password" 
                  placeholder="비밀번호" 
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </FormControl>
              <HStack width="100%" justifyContent="space-between" mt="auto">
                <Button variant="link" onClick={handleToggle}>
                  {isRegistering ? '로그인' : '관리자 신청'}
                </Button>
                <Button type="submit" colorScheme="blue">
                  {isRegistering ? '신청' : '로그인'}
                </Button>
              </HStack>
            </VStack>
          </form>
        </Box>
      </SlideFade>
    </Box>
  );
};

export default LoginScreen;
