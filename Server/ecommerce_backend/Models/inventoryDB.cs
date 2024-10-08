using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace WebServerWithMongoDB.Models
{
    public class Inventory
    {
        public ObjectId Id { get; set; } // Unique identifier for the inventory item
        public string? ProductId { get; set; } // Unique Product ID
        public int Stock { get; set; } // Current stock level
    }
}
