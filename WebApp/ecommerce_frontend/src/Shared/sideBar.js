import React from 'react';
import { Link } from 'react-router-dom';
import { FaHome, FaBox, FaClipboardList,  FaWarehouse} from 'react-icons/fa';
import './sideBar.css'; // Custom CSS for further styling

const Sidebar = () => {
  return (
    <div className="bg-dark text-white d-flex flex-column justify-content-between" style={{ width: '250px', height: '100vh' }}>
      {/* Sidebar Header */}
      <div className="sidebar-header p-4 text-center">
        <h4 className="fw-bold">E-commerce Platform</h4>
      </div>

      {/* Sidebar Menu */}
      <ul className="list-unstyled flex-grow-1">
        <li className="sidebar-item p-3"> {/* Applied class for spacing */}
          <Link to="/" className="text-white text-decoration-none d-flex align-items-center sidebar-link">
            <FaHome className="me-3" /> <span>Home</span>
          </Link>
        </li>
        <li className="sidebar-item p-3"> {/* Applied class for spacing */}
          <Link to="/products" className="text-white text-decoration-none d-flex align-items-center sidebar-link">
            <FaBox className="me-3" /> <span>Products</span>
          </Link>
        </li>
        <li className="sidebar-item p-3"> {/* Applied class for spacing */}
          <Link to="/orders" className="text-white text-decoration-none d-flex align-items-center sidebar-link">
            <FaClipboardList className="me-3" /> <span>Orders</span>
          </Link>
        </li>
        <li className="sidebar-item p-3"> {/* New Inventory item */}
          <Link to="/inventory" className="text-white text-decoration-none d-flex align-items-center sidebar-link">
            <FaWarehouse className="me-3" /> <span>Inventory</span>
          </Link>
        </li>
      </ul>

      {/* Sidebar Footer */}
      {/* <div className="text-center p-4 small text-muted">
        © 2024 My Store
      </div> */}
    </div>
  );
};

export default Sidebar;
