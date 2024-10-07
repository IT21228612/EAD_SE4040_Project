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
}
