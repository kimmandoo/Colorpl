// src/components/ProfileImageUpdate.js
import React, { useState } from 'react';
import api from '../api/api';

function ProfileImageUpdate({ token }) {
  const [file, setFile] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append('file', file);

    try {
      const response = await api.put('/auth/profile/update_image', formData, {
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
      <h2>Update Profile Image</h2>
      <input
        type="file"
        onChange={(e) => setFile(e.target.files[0])}
      />
      <button type="submit">Update</button>
    </form>
  );
}

export default ProfileImageUpdate;
