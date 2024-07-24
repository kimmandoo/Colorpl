// src/api/auth.js
import api from './api';

export const logout = async () => {
  const response = await api.post(
    '/auth/logout',
    {}, // 요청 본문 (여기서는 빈 객체)
    {
      withCredentials: true, // 쿠키와 함께 요청을 보냄
    }
  );

  return response.data;
};
