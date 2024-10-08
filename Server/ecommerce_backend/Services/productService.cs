using MongoDB.Driver;
using WebServerWithMongoDB.Models;

namespace WebServerWithMongoDB.Services
{
    public class ProductService
    {
        private readonly IMongoCollection<Product> _products;
        private readonly IMongoCollection<Inventory> _inventory; // Correct type for inventory

        public ProductService(IMongoDatabase database)
        {
            _products = database.GetCollection<Product>("Products");
            _inventory = database.GetCollection<Inventory>("Inventory"); // Initialize inventory collection
        }

        public async Task<Product> GetProductById(string productId)
        {
            var filter = Builders<Product>.Filter.Eq(p => p.ProductId, productId);
            return await _products.Find(filter).FirstOrDefaultAsync();
        }

        public async Task<List<Product>> GetAllProducts()
        {
            return await _products.Find(_ => true).ToListAsync();
        }

        public async Task CreateProduct(Product product)
        {
            // Validate the product before inserting
            if (product == null || string.IsNullOrEmpty(product.ProductId) || product.Stock < 0)
            {
                throw new ArgumentException("Invalid product data.");
            }

            // Insert the product into the Products collection
            await _products.InsertOneAsync(product);

            // Create the inventory record for the new product
            var inventory = new Inventory 
            { 
                ProductId = product.ProductId, // Ensure this matches the ProductId in Product
                Stock = product.Stock // Set the initial stock
            };

            // Insert the inventory record into the Inventory collection
            await _inventory.InsertOneAsync(inventory);
        }

        public async Task UpdateProduct(string productId, Product product)
        {
            var filter = Builders<Product>.Filter.Eq(p => p.ProductId, productId);
            await _products.ReplaceOneAsync(filter, product);
        }

        public async Task DeleteProduct(string productId)
        {
            var filter = Builders<Product>.Filter.Eq(p => p.ProductId, productId);
            await _products.DeleteOneAsync(filter);
        }

        public async Task<bool> ActivateProduct(string productId)
        {
            try
            {
                var filter = Builders<Product>.Filter.Eq(p => p.ProductId, productId);
                var update = Builders<Product>.Update.Set(p => p.IsActive, true);

                // Update the product
                var result = await _products.UpdateOneAsync(filter, update);

                // Check if the update was successful
                if (result.MatchedCount == 0)
                {
                    Console.WriteLine($"No product found with ProductId: {productId}");
                    return false; // No product matched the provided ID
                }

                Console.WriteLine($"Product with ProductId: {productId} was successfully activated.");
                return result.ModifiedCount > 0;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error activating product with ProductId: {productId}. Exception: {ex.Message}");
                return false;
            }
        }

        public async Task<bool> DeactivateProduct(string productId)
        {
            try
            {
                var filter = Builders<Product>.Filter.Eq(p => p.ProductId, productId);
                var update = Builders<Product>.Update.Set(p => p.IsActive, false);

                // Update the product
                var result = await _products.UpdateOneAsync(filter, update);

                // Check if the update was successful
                if (result.MatchedCount == 0)
                {
                    Console.WriteLine($"No product found with ProductId: {productId}");
                    return false; // No product matched the provided ID
                }

                Console.WriteLine($"Product with ProductId: {productId} was successfully deactivated.");
                return result.ModifiedCount > 0;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error deactivating product with ProductId: {productId}. Exception: {ex.Message}");
                return false;
            }
        }
    }
}
