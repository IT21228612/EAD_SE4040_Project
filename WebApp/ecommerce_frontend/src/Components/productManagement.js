import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Modal, Button } from 'react-bootstrap';

const Products = () => {
  const [products, setProducts] = useState([]);
  const [newProduct, setNewProduct] = useState({
    productId: '',
    name: '',
    category: '',
    price: 0,
    stock: 0,
    vendorId: '',
    vendorName: '',
  });
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState(null);

  // Pagination states
  const [currentPage, setCurrentPage] = useState(1);
  const [productsPerPage] = useState(5);
  const [searchQuery, setSearchQuery] = useState(''); // State for search query

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      const response = await axios.get('http://localhost:5177/api/product');
      setProducts(response.data);
    } catch (error) {
      console.error('Error fetching products:', error);
    }
  };

  const createProduct = async () => {
    try {
      await axios.post('http://localhost:5177/api/product/create', newProduct);
      fetchProducts();
      setNewProduct({
        productId: '',
        name: '',
        category: '',
        price: 0,
        stock: 0,
        vendorId: '',
        vendorName: '',
      });
      setShowCreateModal(false);
    } catch (error) {
      console.error('Error creating product:', error);
    }
  };

  const updateProduct = async () => {
    try {
      await axios.put(`http://localhost:5177/api/product/update/${selectedProduct.productId}`, selectedProduct);
      fetchProducts();
      setShowUpdateModal(false);
    } catch (error) {
      console.error('Error updating product:', error);
    }
  };

  const deleteProduct = async (productId) => {
    await axios.delete(`http://localhost:5177/api/product/delete/${productId}`);
    fetchProducts();
  };

  const activateProduct = async (productId) => {
    await axios.post(`http://localhost:5177/api/product/activate/${productId}`);
    fetchProducts();
  };

  const deactivateProduct = async (productId) => {
    await axios.post(`http://localhost:5177/api/product/deactivate/${productId}`);
    fetchProducts();
  };

  const openUpdateModal = (product) => {
    setSelectedProduct(product);
    setShowUpdateModal(true);
  };

  // Calculate the products for the current page
  const indexOfLastProduct = currentPage * productsPerPage;
  const indexOfFirstProduct = indexOfLastProduct - productsPerPage;

  // Filter products based on the search query
  const filteredProducts = products.filter(product =>
    product.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
    product.category.toLowerCase().includes(searchQuery.toLowerCase())
  );

  // Get current products to display based on filtered products
  const currentProducts = filteredProducts.slice(indexOfFirstProduct, indexOfLastProduct);

  // Determine the total number of pages
  const totalPages = Math.ceil(filteredProducts.length / productsPerPage);

  // Pagination control functions
  const nextPage = () => {
    if (currentPage < totalPages) {
      setCurrentPage(currentPage + 1);
    }
  };

  const prevPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  };

  return (
    <div className="container">
      <h2 className="mb-4">Products</h2>

      {/* Search Bar */}
      <div className="mb-4">
        <input
          type="text"
          className="form-control"
          placeholder="Search by name or category..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </div>

      {/* Button to open Create Product modal */}
      <Button className="mb-4" variant="primary" onClick={() => setShowCreateModal(true)}>
        Add New Product
      </Button>

      {/* Product Table */}
      <table className="table table-striped">
        <thead>
          <tr>
            <th>Product ID</th>
            <th>Name</th>
            <th>Category</th>
            <th>Price</th>
            <th>Stock</th>
            <th>Status</th>
            <th>Vendor Name</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {currentProducts.map((product) => (
            <tr key={product.productId}>
              <td>{product.productId}</td>
              <td>{product.name}</td>
              <td>{product.category}</td>
              <td>Rs. {product.price.toFixed(2)}</td>
              <td>{product.stock}</td>
              <td>{product.isActive ? 'Active' : 'Inactive'}</td>
              <td>{product.vendorName}</td>
              <td>
                <button className="btn btn-warning me-2" onClick={() => openUpdateModal(product)}>
                  Edit
                </button>
                <button className="btn btn-danger me-2" onClick={() => deleteProduct(product.productId)}>
                  Delete
                </button>
                {product.isActive ? (
                  <button className="btn btn-secondary" onClick={() => deactivateProduct(product.productId)}>
                    Deactivate
                  </button>
                ) : (
                  <button className="btn btn-success" onClick={() => activateProduct(product.productId)}>
                    Activate
                  </button>
                )}
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

      {/* Modal for Adding New Product */}
      <Modal show={showCreateModal} onHide={() => setShowCreateModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Add New Product</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <form>
            <div className="mb-3">
              <label className="form-label">Product ID</label>
              <input
                type="text"
                className="form-control"
                value={newProduct.productId}
                onChange={(e) => setNewProduct({ ...newProduct, productId: e.target.value })}
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Name</label>
              <input
                type="text"
                className="form-control"
                value={newProduct.name}
                onChange={(e) => setNewProduct({ ...newProduct, name: e.target.value })}
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Category</label>
              <input
                type="text"
                className="form-control"
                value={newProduct.category}
                onChange={(e) => setNewProduct({ ...newProduct, category: e.target.value })}
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Price</label>
              <input
                type="number"
                className="form-control"
                value={newProduct.price}
                onChange={(e) => setNewProduct({ ...newProduct, price: parseFloat(e.target.value) })}
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Stock</label>
              <input
                type="number"
                className="form-control"
                value={newProduct.stock}
                onChange={(e) => setNewProduct({ ...newProduct, stock: parseInt(e.target.value) })}
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Vendor ID</label>
              <input
                type="text"
                className="form-control"
                value={newProduct.vendorId}
                onChange={(e) => setNewProduct({ ...newProduct, vendorId: e.target.value })}
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Vendor Name</label>
              <input
                type="text"
                className="form-control"
                value={newProduct.vendorName}
                onChange={(e) => setNewProduct({ ...newProduct, vendorName: e.target.value })}
              />
            </div>
            <Button variant="primary" onClick={createProduct}>
              Add Product
            </Button>
          </form>
        </Modal.Body>
      </Modal>

      {/* Modal for Updating Product */}
      <Modal show={showUpdateModal} onHide={() => setShowUpdateModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Update Product</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {selectedProduct && (
            <form>
              <div className="mb-3">
                <label className="form-label">Product ID</label>
                <input
                  type="text"
                  className="form-control"
                  value={selectedProduct.productId}
                  readOnly
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Name</label>
                <input
                  type="text"
                  className="form-control"
                  value={selectedProduct.name}
                  onChange={(e) =>
                    setSelectedProduct({
                      ...selectedProduct,
                      name: e.target.value,
                    })
                  }
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Category</label>
                <input
                  type="text"
                  className="form-control"
                  value={selectedProduct.category}
                  onChange={(e) =>
                    setSelectedProduct({
                      ...selectedProduct,
                      category: e.target.value,
                    })
                  }
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Price</label>
                <input
                  type="number"
                  className="form-control"
                  value={selectedProduct.price}
                  onChange={(e) =>
                    setSelectedProduct({
                      ...selectedProduct,
                      price: parseFloat(e.target.value),
                    })
                  }
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Stock</label>
                <input
                  type="number"
                  className="form-control"
                  value={selectedProduct.stock}
                  onChange={(e) =>
                    setSelectedProduct({
                      ...selectedProduct,
                      stock: parseInt(e.target.value),
                    })
                  }
                />
              </div>
              <Button variant="primary" onClick={updateProduct}>
                Update Product
              </Button>
            </form>
          )}
        </Modal.Body>
      </Modal>
    </div>
  );
};

export default Products;
