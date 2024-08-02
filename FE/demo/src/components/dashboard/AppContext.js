import React, { createContext, useContext, useEffect, useState } from 'react';
import axios from 'axios';

const AppContext = createContext();

export const AppProvider = ({ children }) => {
  const [admin, setAdmin] = useState(null);
  
  useEffect(() => {
    const fetchAdmin = async () => {
      const token = localStorage.getItem('token');
      if (token) {
        try {
          const response = await axios.get('http://localhost:8000/cs/admin', {
            headers: {
              Authorization: `Bearer ${token}`
            }
          });
          setAdmin(response.data);
        } catch (error) {
          console.error('Error fetching admin:', error);
        }
      }
    };
    fetchAdmin();
  }, []);

  return (
    <AppContext.Provider value={{ admin }}>
      {children}
    </AppContext.Provider>
  );
};

export const useAppContext = () => useContext(AppContext);
