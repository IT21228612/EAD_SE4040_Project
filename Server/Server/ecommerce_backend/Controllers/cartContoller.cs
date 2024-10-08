using Microsoft.AspNetCore.Mvc;
using WebServerWithMongoDB.Models;
using WebServerWithMongoDB.Services;

[ApiController]
[Route("api/cart")]
public class CartController : ControllerBase
{
    private readonly CartService _cartService;

    public CartController(CartService cartService)
    {
        _cartService = cartService;
    }

    // Fetch all cart items for a user
    [HttpGet("{userId}")]
    public async Task<IActionResult> GetCartItems(string userId)
    {
        try
        {
            var cartItems = await _cartService.GetCartItemsByUserId(userId);
            if (cartItems == null || !cartItems.Any())
            {
                return NotFound("No cart items found for the user.");
            }

            return Ok(cartItems);
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Internal server error: {ex.Message}");
        }
    }

    // Add an item to the cart
   [HttpPost]
public async Task<IActionResult> AddToCart([FromBody] CartItem cartItem)
{
    if (cartItem == null || string.IsNullOrEmpty(cartItem.ProductId))
    {
        return BadRequest("Invalid cart item details.");
    }

    try
    {
        await _cartService.AddToCart(cartItem);
        return Ok("Item added to cart.");
    }
    catch (Exception ex)
    {
        return StatusCode(500, $"Internal server error: {ex.Message}");
    }
}


    // Remove an item from the cart by item ID
    [HttpDelete("{id}")]
    public async Task<IActionResult> RemoveFromCart(string id)
    {
        try
        {
            bool result = await _cartService.RemoveFromCart(id);
            if (!result)
            {
                return NotFound("Cart item not found.");
            }

            return Ok("Item removed from cart.");
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Internal server error: {ex.Message}");
        }
    }

    // Update quantity of an item in the cart
    [HttpPut("{id}/quantity")]
    public async Task<IActionResult> UpdateCartItemQuantity(string id, [FromBody] int quantity)
    {
        if (quantity <= 0)
        {
            return BadRequest("Quantity must be greater than zero.");
        }

        try
        {
            bool result = await _cartService.UpdateCartItemQuantity(id, quantity);
            if (!result)
            {
                return NotFound("Cart item not found.");
            }

            return Ok("Cart item quantity updated.");
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"Internal server error: {ex.Message}");
        }
    }
}
