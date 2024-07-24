// src/components/ProfileImageUpload.js
import React, { useState } from 'react';
import api from '../api/api';

function ProfileImageUpload({ token }) {
  const [file, setFile] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append('file', file);

    try {
      const response = await api.post('/auth/profile/upload_image', formData, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'multipart/form-data',
        },
      });
      alert(response.data.message);
    } catch (error) {
      alert(error.response.data.detail);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Upload Profile Image</h2>
      <input
        type="file"
        onChange={(e) => setFile(e.target.files[0])}
      />
      <button type="submit">Upload</button>
    </form>
  );
}

export default ProfileImageUpload;
