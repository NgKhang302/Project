import React from 'react';    //thư viện react
import ReactDOM from 'react-dom/client';     //công cụ gắn ract vào HTML (DOM)
import { BrowserRouter } from 'react-router-dom'; // chuyển trang k reload
import App from './App.jsx'; // component chính
import './style.css';

ReactDOM.createRoot(document.getElementById('root')).render(  //vẽ app
  <React.StrictMode> //tìm lõi
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </React.StrictMode>
);
