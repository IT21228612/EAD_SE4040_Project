import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Modal, Button } from 'react-bootstrap';

const StockManagement = () => {
    const [products, setProducts] = useState([]);
    const [stockUpdate, setStockUpdate] = useState({ productId: '', stock: 0 });
    const [showStockModal, setShowStockModal] = useState(false);
    const [selectedProduct, setSelectedProduct] = useState(null);
    
    // Pagination states
    const [currentPage, setCurrentPage] = useState(1);
    const [productsPerPage] = useState(5);
    const [searchQuery, setSearchQuery] = useState('');

    useEffect(() => {
        fetchProducts();
    }, []);

    const fetchProducts = async () => {
        try {
            const response = await axios.get('http://localhost:5177/api/product'); // Adjust to your endpoint
            setProducts(response.data);
        } catch (error) {
            console.error('Error fetching products:', error);
        }
    };

    const handleStockUpdate = async () => {
        try {
            await axios.post('http://localhost:5177/api/inventory/addstock', {
                productId: stockUpdate.productId,
                stock: stockUpdate.stock,
            });
            fetchProducts();
            setStockUpdate({ productId: '', stock: 0 });
            setShowStockModal(false);
        } catch (error) {
            console.error('Error adding stock:', error);
        }
    };

    const openStockModal = (product) => {
        setSelectedProduct(product);
        setStockUpdate({ productId: product.productId, stock: 0 });
        setShowStockModal(true);
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
            <h2 className="mb-4">Stock Management</h2>

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

            {/* Product Table */}
            <table className="table table-striped">
                <thead>
                    <tr>
                        <th>Product ID</th>
                        <th>Name</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Stock</th>
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
                            <td>
                                <Button className="btn btn-success me-2" onClick={() => openStockModal(product)}>
                                    Add Stock
                                </Button>
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

            {/* Modal for Adding Stock */}
            <Modal show={showStockModal} onHide={() => setShowStockModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Add Stock for {selectedProduct?.name}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <form>
                        <div className="mb-3">
                            <label className="form-label">Product ID</label>
                            <input
                                type="text"
                                className="form-control"
                                value={stockUpdate.productId}
                                readOnly
                            />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Stock Quantity</label>
                            <input
                                type="number"
                                className="form-control"
                                value={stockUpdate.stock}
                                onChange={(e) => setStockUpdate({ ...stockUpdate, stock: parseInt(e.target.value) })}
                            />
                        </div>
                        <Button variant="primary" onClick={handleStockUpdate}>
                            Update Stock
                        </Button>
                    </form>
                </Modal.Body>
            </Modal>
        </div>
    );
};

export default StockManagement;
