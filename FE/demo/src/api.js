import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8000', // 백엔드 서버의 기본 URL
});

api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

export default api;
