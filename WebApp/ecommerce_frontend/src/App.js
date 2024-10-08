import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Sidebar from './Shared/sideBar';
import Products from './Components/productManagement';
import Orders from './Components/orderManagement';
import Home from './Components/HomeScreen';
import Inventory from './Components/stockManagement';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  return (
    <Router>
      <div className="app-container d-flex">
        <Sidebar />
        <div className="content-container flex-grow-1 p-4" style={{ backgroundColor: '#f4f4f9', height: '100vh' }}>
          <Routes>
          <Route path="/" element={<Home />} />
            <Route path="/products" element={<Products />} />
            <Route path="/orders" element={<Orders />} />
            <Route path="/inventory" element={<Inventory />} />
            {/* <Route path="/" element={<h2>Welcome to the Dashboard</h2>} /> */}
            {/* Add more routes for orders, etc */}
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;
