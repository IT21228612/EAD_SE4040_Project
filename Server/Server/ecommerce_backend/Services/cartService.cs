using MongoDB.Bson; // Ensure this is included for ObjectId
using MongoDB.Driver;
using WebServerWithMongoDB.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

public class CartService
{
    private readonly IMongoCollection<CartItem> _cartItems;

    public CartService(IMongoDatabase database)
    {
        _cartItems = database.GetCollection<CartItem>("CartItems");
    }

    // Fetch cart items for a specific user
    public async Task<List<CartItem>> GetCartItemsByUserId(string userId)
    {
        // Validate userId
        if (string.IsNullOrEmpty(userId))
        {
            throw new ArgumentException("UserId cannot be null or empty.", nameof(userId));
        }

        // Create a filter for the UserId field
        var filter = Builders<CartItem>.Filter.Eq("UserId", userId);
        
        return await _cartItems.Find(filter).ToListAsync();
    }

    // Add a new item to the cart
    public async Task AddToCart(CartItem cartItem)
    {
        if (cartItem == null)
        {
            throw new ArgumentNullException(nameof(cartItem), "Cart item cannot be null.");
        }

        await _cartItems.InsertOneAsync(cartItem);
    }

    // Remove an item from the cart by ID
    public async Task<bool> RemoveFromCart(string id)
    {
        if (string.IsNullOrEmpty(id))
        {
            throw new ArgumentException("ID cannot be null or empty.", nameof(id));
        }

        var filter = Builders<CartItem>.Filter.Eq("_id", ObjectId.Parse(id)); // Ensure the ID is parsed to ObjectId
        var result = await _cartItems.DeleteOneAsync(filter);
        
        return result.DeletedCount > 0; // Return true if an item was deleted
    }

    // Update the quantity of an item in the cart
    public async Task<bool> UpdateCartItemQuantity(string id, int quantity)
    {
        if (string.IsNullOrEmpty(id))
        {
            throw new ArgumentException("ID cannot be null or empty.", nameof(id));
        }

        if (quantity <= 0)
        {
            throw new ArgumentException("Quantity must be greater than zero.", nameof(quantity));
        }

        var filter = Builders<CartItem>.Filter.Eq("_id", ObjectId.Parse(id)); // Parse ID to ObjectId
        var update = Builders<CartItem>.Update.Set("Quantity", quantity); // Update quantity

        var result = await _cartItems.UpdateOneAsync(filter, update);
        
        return result.ModifiedCount > 0; // Return true if the quantity was updated
    }
}
