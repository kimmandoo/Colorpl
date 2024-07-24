// src/api/api.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8000',  // FastAPI 서버 URL
});

export default api;
