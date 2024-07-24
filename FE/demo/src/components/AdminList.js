import React, { useState, useEffect } from 'react';
import axios from 'axios';

const AdminList = () => {
  const [admins, setAdmins] = useState([]);

  useEffect(() => {
    const fetchAdmins = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get('http://localhost:8000/admin/administrators', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        setAdmins(response.data);
      } catch (error) {
        alert('Failed to fetch admin list');
      }
    };

    fetchAdmins();
  }, []);

  return (
    <div>
      <h2>Admin List</h2>
      <ul>
        {admins.map(admin => (
          <li key={admin.id}>{admin.username} - {admin.email} - {admin.role} - {admin.approved ? 'Approved' : 'Not Approved'}</li>
        ))}
      </ul>
    </div>
  );
};

export default AdminList;
