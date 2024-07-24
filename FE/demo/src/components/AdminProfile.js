// src/components/AdminProfile.js
import React from 'react';
import ProfileImageUpload from './ProfileImageUpload';
import ProfileImageUpdate from './ProfileImageUpdate';

function AdminProfile({ token }) {
  return (
    <div>
      <h2>Admin Profile</h2>
      <ProfileImageUpload token={token} />
      <ProfileImageUpdate token={token} />
    </div>
  );
}

export default AdminProfile;
