using System.ComponentModel.DataAnnotations;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace WebServerWithMongoDB.Models
{
    public class Order
    {
        public ObjectId Id { get; set; } // Unique identifier for the order
        public string? OrderId { get; set; } // Order ID
        public string? CustomerId { get; set; } // Customer ID

        [Required]
        public List<OrderProduct>? Products { get; set; } // List of products in the order
        public string? Status { get; set; } // Status of the order

        public decimal TotalPrice { get; set; }
        public DateTime CreatedAt { get; set; } // Date and time when the order was created
    }

    // OrderProduct class to represent a product in an order, including quantity
    public class OrderProduct
    {
        public string? ProductId { get; set; } // Product ID
        public string? Name { get; set; } // Name of the product
        public decimal Price { get; set; } // Price of the product
        public int Quantity { get; set; } // Quantity of the product in the order
    }
}
