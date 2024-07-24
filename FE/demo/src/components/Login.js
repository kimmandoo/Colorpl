// src/components/Login.js
import React, { useState } from 'react';
import api from '../api/api';

function Login({ setToken }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await api.post('/auth/token', new URLSearchParams({
        username,
        password,
      }));
      const token = response.data.access_token;
      const tokenPayload = JSON.parse(atob(token.split('.')[1]));
      setToken(token, tokenPayload.role);
    } catch (error) {
      alert(error.response.data.detail);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Login</h2>
      <input
        type="text"
        placeholder="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <button type="submit">Login</button>
    </form>
  );
}

export default Login;
