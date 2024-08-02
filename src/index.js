import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import { ChakraProvider, extendTheme } from '@chakra-ui/react';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

const theme = extendTheme({});

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <BrowserRouter>
      <ChakraProvider theme={theme}>
        <App />
      </ChakraProvider>
    </BrowserRouter>
  </React.StrictMode>
);

reportWebVitals();
