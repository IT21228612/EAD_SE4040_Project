import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Modal, Button, Form } from 'react-bootstrap';

const Orders = () => {
  const [products, setProducts] = useState([]);
  const [orders, setOrders] = useState([]);
  const [newOrder, setNewOrder] = useState({
    customerId: '',
    products: [],
    status: '',
    createdAt: '',
  });
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [ordersPerPage] = useState(10);
  const [searchQuery, setSearchQuery] = useState('');
  const [quantities, setQuantities] = useState({});

  useEffect(() => {
    fetchProducts();
    fetchOrders();
  }, []);

  const fetchProducts = async () => {
    try {
      const response = await axios.get('http://localhost:5177/api/product');
      setProducts(response.data);
    } catch (error) {
      console.error('Error fetching products:', error);
    }
  };

  const fetchOrders = async () => {
    try {
      const response = await axios.get('http://localhost:5177/api/order');
      setOrders(response.data);
    } catch (error) {
      console.error('Error fetching orders:', error);
    }
  };

  const createOrder = async () => {
    try {
      newOrder.createdAt = new Date().toISOString();
      newOrder.status = 'Processing';

      await axios.post('http://localhost:5177/api/order/create', newOrder);
      fetchOrders();
      setNewOrder({ customerId: '', products: [], status: '', createdAt: '' });
      setQuantities({});
      setShowCreateModal(false);
    } catch (error) {
      console.error('Error creating order:', error);
    }
  };

  const updateOrder = async () => {
    try {
      await axios.put(`http://localhost:5177/api/order/update/${selectedOrder.orderId}`, selectedOrder);
      fetchOrders();
      setShowUpdateModal(false);
    } catch (error) {
      console.error('Error updating order:', error);
    }
  };

  const cancelOrder = async (orderId) => {
    await axios.post(`http://localhost:5177/api/order/cancel/${orderId}`);
    fetchOrders();
  };

  const openUpdateModal = (order) => {
    setSelectedOrder(order);
    setShowUpdateModal(true);
  };

  // Pagination logic
  const indexOfLastOrder = currentPage * ordersPerPage;
  const indexOfFirstOrder = indexOfLastOrder - ordersPerPage;
  const filteredOrders = orders.filter(order =>
    order.customerId.toLowerCase().includes(searchQuery.toLowerCase()) ||
    order.status.toLowerCase().includes(searchQuery.toLowerCase())
  );
  const currentOrders = filteredOrders.slice(indexOfFirstOrder, indexOfLastOrder);
  const totalPages = Math.ceil(filteredOrders.length / ordersPerPage);

  const nextPage = () => {
    if (currentPage < totalPages) setCurrentPage(currentPage + 1);
  };

  const prevPage = () => {
    if (currentPage > 1) setCurrentPage(currentPage - 1);
  };

  // Handle quantity change
  const handleQuantityChange = (productId, quantity) => {
    setQuantities(prevQuantities => ({
      ...prevQuantities,
      [productId]: quantity > 0 ? parseInt(quantity) : 0,
    }));
  };

  const handleAddProduct = (product) => {
    const quantity = quantities[product.productId] || 0;
    if (quantity > 0) {
      const existingProduct = newOrder.products.find(p => p.productId === product.productId);
      if (existingProduct) {
        existingProduct.quantity += quantity;
      } else {
        newOrder.products.push({ productId: product.productId, name: product.name, quantity });
      }
      setQuantities(prevQuantities => ({ ...prevQuantities, [product.productId]: 0 }));
      setNewOrder({ ...newOrder });
    } else {
      alert('Please enter a valid quantity.');
    }
  };

  const handleRemoveProduct = (productId) => {
    setNewOrder(prevNewOrder => ({
      ...prevNewOrder,
      products: prevNewOrder.products.filter(p => p.productId !== productId),
    }));
  };

  const handleStatusChange = (e) => {
    setSelectedOrder({ ...selectedOrder, status: e.target.value });
  };

  return (
    <div className="container">
      <h2 className="mb-4">Order Management</h2>

      {/* Search Bar */}
      <div className="mb-4">
        <input
          type="text"
          className="form-control"
          placeholder="Search by customer ID or status..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </div>

      {/* Button to open Create Order modal */}
      <Button className="mb-4" variant="primary" onClick={() => setShowCreateModal(true)}>
        Create New Order
      </Button>

      {/* Orders Table */}
      <table className="table table-striped">
        <thead>
          <tr>
            <th>Order ID</th>
            <th>Customer ID</th>
            <th>Products</th>
            <th>Status</th>
            <th>Order Date</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {currentOrders.map((order) => (
            <tr key={order.orderId}>
              <td>{order.orderId}</td>
              <td>{order.customerId}</td>
              <td>{order.products.map(p => `${p.name} (x${p.quantity})`).join(', ')}</td>
              <td>{order.status}</td>
              <td>{new Date(order.createdAt).toLocaleString()}</td>
              <td>
                <button className="btn btn-warning me-2" onClick={() => openUpdateModal(order)}>
                  Edit
                </button>
                <button className="btn btn-danger me-2" onClick={() => cancelOrder(order.orderId)}>
                  Cancel
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Pagination Controls */}
      <div className="d-flex justify-content-between align-items-center">
        <Button variant="secondary" onClick={prevPage} disabled={currentPage === 1}>
          Previous
        </Button>
        <span>Page {currentPage} of {totalPages}</span>
        <Button variant="secondary" onClick={nextPage} disabled={currentPage === totalPages}>
          Next
        </Button>
      </div>

      {/* Modal for Creating New Order */}
      <Modal show={showCreateModal} onHide={() => setShowCreateModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Create New Order</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <form>
            <div className="mb-3">
              <label className="form-label">Customer ID</label>
              <input
                type="text"
                className="form-control"
                value={newOrder.customerId}
                onChange={(e) => setNewOrder({ ...newOrder, customerId: e.target.value })}
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Select Products</label>
              <div className="list-group">
                {products.map(product => (
                  <div key={product.productId} className="list-group-item">
                    <div className="d-flex justify-content-between align-items-center">
                      <span>{product.name} (Stock: {product.stock})</span>
                      <input
                        type="number"
                        min="0"
                        placeholder="Quantity"
                        className="form-control"
                        style={{ width: '100px' }}
                        value={quantities[product.productId] || 0}
                        onChange={(e) => handleQuantityChange(product.productId, e.target.value)}
                      />
                      <Button variant="success" onClick={() => handleAddProduct(product)} className="ms-2">
                        Add
                      </Button>
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {/* Display Added Products */}
            <h5>Added Products:</h5>
            <ul className="list-group mb-3">
              {newOrder.products.map(item => (
                <li key={item.productId} className="list-group-item d-flex justify-content-between align-items-center">
                  {item.name} (x{item.quantity})
                  <Button variant="danger" onClick={() => handleRemoveProduct(item.productId)}>
                    Remove
                  </Button>
                </li>
              ))}
            </ul>

            <Button variant="primary" onClick={createOrder}>
              Create Order
            </Button>
          </form>
        </Modal.Body>
      </Modal>

      {/* Modal for Updating Order */}
      <Modal show={showUpdateModal} onHide={() => setShowUpdateModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Update Order</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {selectedOrder && (
            <form>
              <div className="mb-3">
                <label className="form-label">Order ID</label>
                <input
                  type="text"
                  className="form-control"
                  value={selectedOrder.orderId}
                  readOnly
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Customer ID</label>
                <input
                  type="text"
                  className="form-control"
                  value={selectedOrder.customerId}
                  onChange={(e) =>
                    setSelectedOrder({ ...selectedOrder, customerId: e.target.value })
                  }
                />
              </div>

              {/* Status Dropdown */}
              <div className="mb-3">
                <label className="form-label">Status</label>
                <Form.Select value={selectedOrder.status} onChange={handleStatusChange}>
                  <option value="Processing">Processing</option>
                  <option value="Ready">Ready</option>
                  <option value="Delivered">Delivered</option>
                  <option value="Completed">Completed</option>
                </Form.Select>
              </div>

              {/* Product List in Update Modal */}
              <div className="mb-3">
                <label className="form-label">Products</label>
                <ul className="list-group">
                  {selectedOrder.products.map((item) => (
                    <li key={item.productId} className="list-group-item d-flex justify-content-between align-items-center">
                      {item.name} (x{item.quantity})
                      <Button variant="danger" onClick={() => handleRemoveProduct(item.productId)}>
                        Remove
                      </Button>
                    </li>
                  ))}
                </ul>
              </div>

              <Button variant="primary" onClick={updateOrder}>
                Update Order
              </Button>
            </form>
          )}
        </Modal.Body>
      </Modal>
    </div>
  );
};

export default Orders;
