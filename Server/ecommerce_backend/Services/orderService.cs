using MongoDB.Driver;
using WebServerWithMongoDB.Models;

namespace WebServerWithMongoDB.Services
{

public class OrderService
{
     private readonly IMongoCollection<Order> _orders;
        private readonly IMongoCollection<Product> _products;

        public OrderService(IMongoDatabase database)
        {
            _orders = database.GetCollection<Order>("Orders");
            _products = database.GetCollection<Product>("Products");
        }

   public async Task CreateOrder(Order order)
{
    // Set initial order details
    order.CreatedAt = DateTime.UtcNow;
    order.Status = "Processing";

    // Get the last order and extract the numeric part of the Order ID
    var lastOrder = await _orders.Find(Builders<Order>.Filter.Empty)
                                .SortByDescending(o => o.OrderId)
                                .FirstOrDefaultAsync();
                                
    int nextOrderId;
    if (lastOrder != null && lastOrder.OrderId.StartsWith("ORD"))
    {
        // Extract numeric part by removing the prefix (e.g., "ORD123" -> "123")
        var numericPart = lastOrder.OrderId.Substring(3); // Remove "ORD"
        nextOrderId = int.Parse(numericPart) + 1; // Increment the numeric part
    }
    else
    {
        nextOrderId = 1; // If no previous order, start from 1
    }

    // Format the Order ID with "ORD" prefix and ensure it has leading zeros
    order.OrderId = $"ORD{nextOrderId:D1}"; 

    // Check and fill product details from the product database
    var orderProducts = new List<OrderProduct>();

    // Ensure that order.Products is not null
    if (order.Products == null || !order.Products.Any())
    {
        throw new Exception("Order must contain at least one product.");
    }

    // Initialize total price variable
    decimal totalPrice = 0m;

    foreach (var orderProduct in order.Products)
    {
        // Fetch product details from the Products collection
        var productFromDb = await _products.Find(p => p.ProductId == orderProduct.ProductId).FirstOrDefaultAsync();

        if (productFromDb == null)
        {
            throw new Exception($"Product with ID {orderProduct.ProductId} does not exist.");
        }

        // Check if there is enough stock
        if (productFromDb.Stock < orderProduct.Quantity)
        {
            throw new Exception($"Insufficient stock for product {productFromDb.Name}. Available stock: {productFromDb.Stock}");
        }

        // Calculate the price for this product
        decimal productPrice = productFromDb.Price * orderProduct.Quantity;
        totalPrice += productPrice;

        // Add product details to the order
        orderProducts.Add(new OrderProduct
        {
            ProductId = productFromDb.ProductId,
            Name = productFromDb.Name,
            Price = productFromDb.Price,
            Quantity = orderProduct.Quantity // Use the quantity requested in the order
        });

        // Decrement the stock in the Products collection
        var updateStock = Builders<Product>.Update.Inc(p => p.Stock, -orderProduct.Quantity);
        await _products.UpdateOneAsync(p => p.ProductId == productFromDb.ProductId, updateStock);
    }

    // Assign the filled product details to the order
    order.Products = orderProducts;

    // Assign the total price to the order
    order.TotalPrice = totalPrice;

    // Insert the order into the Orders collection
    await _orders.InsertOneAsync(order);
}
    public async Task UpdateOrder(string orderId, Order updatedOrder)
{
    var filter = Builders<Order>.Filter.Eq(o => o.OrderId, orderId);
    
    var update = Builders<Order>.Update
        // .Set(o => o.CustomerId, updatedOrder.CustomerId)
        .Set(o => o.Products, updatedOrder.Products)
        .Set(o => o.Status, updatedOrder.Status)
        .Set(o => o.CreatedAt, updatedOrder.CreatedAt);
    
    await _orders.UpdateOneAsync(filter, update); 
}


    public async Task CancelOrder(string orderId)
    {
        var filter = Builders<Order>.Filter.Eq(o => o.OrderId, orderId);
        var update = Builders<Order>.Update.Set(o => o.Status, "Cancelled");
        await _orders.UpdateOneAsync(filter, update);
    }

     public async Task<List<Order>> GetAllOrders()
        {
            // Retrieves all orders from the Orders collection
            return await _orders.Find(_ => true).ToListAsync();
        }
}
}