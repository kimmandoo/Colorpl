// pages/LoginScreen.jsx
import React, { useState } from 'react';
import { Box, Button, TextField, Typography, Container, Snackbar, Alert, CircularProgress } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import api from '../api';

const LoginScreen = () => {
  const [isRegistering, setIsRegistering] = useState(false);
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleCloseSnackbar = () => {
    setOpenSnackbar(false);
  };

  const resetForm = () => {
    setUsername('');
    setEmail('');
    setPassword('');
  };

  const handleLogin = async (event) => {
    event.preventDefault();
    setLoading(true);
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
      navigate('/dashboard');
    } catch (error) {
      const errorMsg = error.response?.data?.detail || '로그인 실패';
      setMessage(errorMsg);
      setOpenSnackbar(true);
      resetForm();
    } finally {
      setLoading(false);
    }
  };

  const handleSignup = async (event) => {
    event.preventDefault();
    setLoading(true);
    try {
      const response = await api.post('/auth/admin/signup', {
        username,
        email,
        password
      });
      console.log(response.data);
      setMessage(response.data.message);
      setOpenSnackbar(true);
      resetForm();
    } catch (error) {
      const errorMsg = error.response?.data?.detail || '관리자 신청 실패';
      setMessage(errorMsg);
      setOpenSnackbar(true);
      resetForm();
    } finally {
      setLoading(false);
    }
  };

  const handleToggle = () => {
    setIsRegistering(!isRegistering);
    setMessage(''); // 상태 변경 시 메시지 초기화
  };

  return (
    <Container component="main" maxWidth="xs" sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', minHeight: '100vh' }}>
      {/* Snackbar Component - Displayed Above the Form */}
      <Snackbar
        open={openSnackbar}
        autoHideDuration={6000}
        onClose={handleCloseSnackbar}
        sx={{ position: 'absolute', top: 20, width: '100%' }} // Positioning Snackbar above the form
      >
        <Alert
          onClose={handleCloseSnackbar}
          severity="error"
          sx={{
            width: '100%',
            maxWidth: '400px', // Set maximum width to control size
            mx: 'auto', // Center horizontally
            fontSize: '1.1rem', // Increase font size for better readability
            padding: 2, // Increase padding for better appearance
          }}
        >
          {message}
        </Alert>
      </Snackbar>

      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          padding: 3,
          border: '1px solid',
          borderRadius: 2,
          boxShadow: 3,
          width: '100%',
          maxWidth: 400,
          mt: 10, // Margin to ensure it's below the Snackbar
          mb: 5 // Margin to ensure spacing from bottom
        }}
      >
        <Typography variant="h5" component="h1">
          {isRegistering ? '관리자 신청' : '로그인'}
        </Typography>
        <Box component="form" onSubmit={isRegistering ? handleSignup : handleLogin} sx={{ mt: 2 }}>
          <TextField
            margin="normal"
            required
            fullWidth
            label={isRegistering ? '이름' : '아이디'}
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          {isRegistering && (
            <TextField
              margin="normal"
              required
              fullWidth
              label="이메일"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          )}
          <TextField
            margin="normal"
            required
            fullWidth
            label="비밀번호"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <Button
            type="submit"
            fullWidth
            variant="contained"
            color="primary"
            sx={{ mt: 2 }}
            disabled={loading}
          >
            {loading ? <CircularProgress size={24} /> : isRegistering ? '신청' : '로그인'}
          </Button>
          <Button
            fullWidth
            variant="text"
            color="primary"
            sx={{ mt: 2 }}
            onClick={handleToggle}
          >
            {isRegistering ? '로그인' : '관리자 신청'}
          </Button>
        </Box>
      </Box>
    </Container>
  );
};

export default LoginScreen;
