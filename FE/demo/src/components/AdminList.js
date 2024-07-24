// src/components/AdminList.js
import React, { useState, useEffect } from 'react';
import api from '../api/api';

function AdminList({ token }) {
  const [admins, setAdmins] = useState([]);
  const [selectedRole, setSelectedRole] = useState({});

  useEffect(() => {
    const fetchAdmins = async () => {
      try {
        const response = await api.get('/admin/administrators', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        setAdmins(response.data);
      } catch (error) {
        alert(error.response.data.detail);
      }
    };

    fetchAdmins();
  }, [token]);

  const approveAdmin = async (adminId) => {
    try {
      const response = await api.post(`/admin/administrators/${adminId}/approve`, { role: selectedRole[adminId] }, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      alert(response.data.message);
      setAdmins(admins.map(admin => 
        admin.id === adminId ? { ...admin, status: { ...admin.status, approved: true } } : admin
      ));
    } catch (error) {
      alert(error.response.data.detail);
    }
  };

  const handleRoleChange = (adminId, role) => {
    setSelectedRole({
      ...selectedRole,
      [adminId]: role
    });
  };

  return (
    <div>
      <h2>Admin List</h2>
      <ul>
        {admins.map(admin => (
          <li key={admin.id}>
            {admin.username} ({admin.email}) - Approved: {admin.status.approved ? "Yes" : "No"}
            {!admin.status.approved && (
              <div>
                <select onChange={(e) => handleRoleChange(admin.id, e.target.value)} value={selectedRole[admin.id] || ""}>
                  <option value="">Select Role</option>
                  <option value="0">Admin</option>
                  <option value="1">Super Admin</option>
                </select>
                <button onClick={() => approveAdmin(admin.id)}>Approve</button>
              </div>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default AdminList;
