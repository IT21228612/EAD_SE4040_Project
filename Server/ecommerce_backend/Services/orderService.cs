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

            // Check and fill product details from the product database
            var orderProducts = new List<Product>();
#pragma warning disable CS8602 // Dereference of a possibly null reference.
            foreach (var orderProduct in order.Products)
            {
                // Fetch product details from the Products collection
                var productFromDb = await _products.Find(p => p.ProductId == orderProduct.ProductId).FirstOrDefaultAsync();

                if (productFromDb == null)
                {
                    throw new Exception($"Product with ID {orderProduct.ProductId} does not exist.");
                }

                // Check if there is enough stock
                if (productFromDb.Stock < orderProduct.Stock)
                {
                    throw new Exception($"Insufficient stock for product {productFromDb.Name}. Available stock: {productFromDb.Stock}");
                }

                // Add product details to the order
                orderProducts.Add(new Product
                {
                    ProductId = productFromDb.ProductId,
                    Name = productFromDb.Name,
                    Category = productFromDb.Category,
                    Price = productFromDb.Price,
                    IsActive = productFromDb.IsActive,
                    VendorId = productFromDb.VendorId,
                    VendorName = productFromDb.VendorName,
                    Stock = orderProduct.Stock // Use the quantity requested in the order
                });

                // Optionally, you may want to decrement the stock in the Products collection
                var updateStock = Builders<Product>.Update.Inc(p => p.Stock, -orderProduct.Stock);
                await _products.UpdateOneAsync(p => p.ProductId == productFromDb.ProductId, updateStock);
            }
#pragma warning restore CS8602 // Dereference of a possibly null reference.

            // Assign the filled product details to the order
            order.Products = orderProducts;

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
}
}