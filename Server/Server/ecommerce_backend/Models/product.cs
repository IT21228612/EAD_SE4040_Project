using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace WebServerWithMongoDB.Models
{public class Product
{
    public ObjectId Id { get; set; } // MongoDB ObjectId
    public string? ProductId { get; set; } // Unique Product ID
    public string? Name { get; set; }
    public string? Category { get; set; } // Category of the product
    public bool IsActive { get; set; } // For activation/deactivation
    public decimal Price { get; set; }
    public int Stock { get; set; } // Stock Quantity
    public string? VendorId { get; set; } // Vendor who owns the product
    public string? VendorName { get; set; }
}
}