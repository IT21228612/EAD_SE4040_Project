using Microsoft.AspNetCore.Mvc;
using WebServerWithMongoDB.Services;
using MongoDB.Driver;
using WebServerWithMongoDB.Models;

[ApiController]
[Route("api/[controller]")]
public class OrderController : ControllerBase
{
    private readonly OrderService _orderService;

    public OrderController(OrderService orderService)
    {
        _orderService = orderService;
    }

    [HttpPost("create")]
    public async Task<IActionResult> CreateOrder([FromBody] Order order)
    {
        await _orderService.CreateOrder(order);
        return Ok("Order created successfully");
    }

    [HttpPut("update/{orderId}")]
    public async Task<IActionResult> UpdateOrder(string orderId, [FromBody] Order order)
    {
        await _orderService.UpdateOrder(orderId, order);
        return Ok("Order updated successfully");
    }

    [HttpDelete("cancel/{orderId}")]
    public async Task<IActionResult> CancelOrder(string orderId)
    {
        await _orderService.CancelOrder(orderId);
        return Ok("Order cancelled successfully");
    }

[HttpGet("customer/{customerId}")]
public async Task<IActionResult> GetOrdersByCustomer(string customerId)
{
    var orders = await _orderService.GetOrdersByCustomerId(customerId);
    if (orders == null || !orders.Any())
        return NotFound("No orders found for this customer.");
    return Ok(orders);
}

    
    // Fetch a specific order by ID
    [HttpGet("{orderId}")]
    public async Task<IActionResult> GetOrderById(string orderId)
    {
        var order = await _orderService.GetOrderById(orderId);
        if (order == null)
            return NotFound("Order not found.");
        return Ok(order);
    }
    

    [HttpGet("all")]
public async Task<IActionResult> GetAllOrders()
{
    var orders = await _orderService.GetAllOrders(); // Fetch all orders
    if (orders == null || !orders.Any())
    {
        return NotFound("No orders found."); // Return 404 if no orders exist
    }

    return Ok(orders); // Return 200 OK with the list of orders
}

        

}
