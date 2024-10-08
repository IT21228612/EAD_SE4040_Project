using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace WebServerWithMongoDB.Models
{
    public class CartItem
    {
        [BsonId]
        public ObjectId Id { get; set; } // MongoDB ObjectId
        public string ProductId { get; set; } // Product ID
        public int Quantity { get; set; } // Quantity of the product
        public string UserId { get; set; } // ID of the user who owns this cart item
        public string Name { get; set; } // Name of the product
        public double Price { get; set; } // Price of the product
    }
}
