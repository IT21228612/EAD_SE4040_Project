// src/HomeScreen.js
import React from 'react';
import { Container, Row, Col, Navbar, Nav, Button, Card } from 'react-bootstrap';

const HomeScreen = () => {
  return (
    <div>
      {/* Header Section */}
      <Navbar bg="light" expand="lg">
        <Container>
          <Navbar.Brand href="#home">MyShop</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link href="#home">Home</Nav.Link>
              <Nav.Link href="#shop">Shop</Nav.Link>
              <Nav.Link href="#about">About Us</Nav.Link>
            </Nav>
            <form className="d-flex">
              <input
                type="search"
                placeholder="Search for products..."
                className="form-control me-2"
                aria-label="Search"
              />
              <Button variant="outline-success">Search</Button>
            </form>
            <Nav>
              <Nav.Link href="#profile"><i className="fas fa-user"></i></Nav.Link>
              <Nav.Link href="#cart"><i className="fas fa-shopping-cart"></i> <span className="badge bg-danger">3</span></Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>

      {/* Hero Section */}
      <div className="hero bg-primary text-white text-center py-5">
        <Container>
          <h1>Welcome to MyShop</h1>
          <p>Your one-stop shop for everything!</p>
          <Button variant="light" href="#shop">Shop Now</Button>
        </Container>
      </div>

      {/* Featured Categories Section */}
      <Container className="my-5">
        <h2 className="text-center mb-4">Featured Categories</h2>
        <Row>
          <Col md={4} className="mb-4">
            <Card>
              <Card.Img variant="top" src="https://via.placeholder.com/300" />
              <Card.Body>
                <Card.Title>Electronics</Card.Title>
                <Button variant="primary" href="#electronics">Shop Now</Button>
              </Card.Body>
            </Card>
          </Col>
          <Col md={4} className="mb-4">
            <Card>
              <Card.Img variant="top" src="https://via.placeholder.com/300" />
              <Card.Body>
                <Card.Title>Fashion</Card.Title>
                <Button variant="primary" href="#fashion">Shop Now</Button>
              </Card.Body>
            </Card>
          </Col>
          <Col md={4} className="mb-4">
            <Card>
              <Card.Img variant="top" src="https://via.placeholder.com/300" />
              <Card.Body>
                <Card.Title>Home Appliances</Card.Title>
                <Button variant="primary" href="#home-appliances">Shop Now</Button>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>

      {/* Featured Products Section */}
      <Container className="my-5">
        <h2 className="text-center mb-4">Featured Products</h2>
        <Row>
          <Col md={3} className="mb-4">
            <Card>
              <Card.Img variant="top" src="https://via.placeholder.com/200" />
              <Card.Body>
                <Card.Title>Product 1</Card.Title>
                <Card.Text>$29.99</Card.Text>
                <Button variant="primary">Add to Cart</Button>
              </Card.Body>
            </Card>
          </Col>
          <Col md={3} className="mb-4">
            <Card>
              <Card.Img variant="top" src="https://via.placeholder.com/200" />
              <Card.Body>
                <Card.Title>Product 2</Card.Title>
                <Card.Text>$49.99</Card.Text>
                <Button variant="primary">Add to Cart</Button>
              </Card.Body>
            </Card>
          </Col>
          <Col md={3} className="mb-4">
            <Card>
              <Card.Img variant="top" src="https://via.placeholder.com/200" />
              <Card.Body>
                <Card.Title>Product 3</Card.Title>
                <Card.Text>$19.99</Card.Text>
                <Button variant="primary">Add to Cart</Button>
              </Card.Body>
            </Card>
          </Col>
          <Col md={3} className="mb-4">
            <Card>
              <Card.Img variant="top" src="https://via.placeholder.com/200" />
              <Card.Body>
                <Card.Title>Product 4</Card.Title>
                <Card.Text>$39.99</Card.Text>
                <Button variant="primary">Add to Cart</Button>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default HomeScreen;
