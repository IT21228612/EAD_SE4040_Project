using System.ComponentModel.DataAnnotations;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace WebServerWithMongoDB.Models
{
public class Order
{
    public ObjectId Id { get; set; }
    public string? OrderId { get; set; } 
    public string? CustomerId { get; set; }

    [Required]
    public List<Product>? Products { get; set; }
    public string? Status { get; set; } 
    public DateTime CreatedAt { get; set; }
}
}