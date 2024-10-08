using MongoDB.Driver;
using WebServerWithMongoDB.Models;

namespace WebServerWithMongoDB.Services
{
    public class InventoryService
    {
        private readonly IMongoCollection<Inventory> _inventory;

        public InventoryService(IMongoDatabase database)
        {
            _inventory = database.GetCollection<Inventory>("Inventory");
        }

        // Add stock to an existing product in inventory
        public async Task<bool> AddStock(string productId, int quantity)
        {
            try
            {
                var filter = Builders<Inventory>.Filter.Eq(i => i.ProductId, productId);
                var update = Builders<Inventory>.Update.Inc(i => i.Stock, quantity);

                var result = await _inventory.UpdateOneAsync(filter, update);
                return result.ModifiedCount > 0;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error adding stock for ProductId: {productId}. Exception: {ex.Message}");
                return false;
            }
        }

        // Get the current stock level for a specific product
        public async Task<int> GetStock(string productId)
        {
            var inventoryItem = await _inventory.Find(i => i.ProductId == productId).FirstOrDefaultAsync();
            return inventoryItem?.Stock ?? 0; // Return 0 if no inventory item found
        }
    }
}
