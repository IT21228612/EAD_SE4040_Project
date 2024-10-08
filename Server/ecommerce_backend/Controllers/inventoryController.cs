using Microsoft.AspNetCore.Mvc;
using WebServerWithMongoDB.Models;
using WebServerWithMongoDB.Services;

namespace WebServerWithMongoDB.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class InventoryController : ControllerBase
    {
        private readonly InventoryService _inventoryService;

        public InventoryController(InventoryService inventoryService)
        {
            _inventoryService = inventoryService;
        }

        // POST: api/inventory/addstock
        [HttpPost("addstock")]
        public async Task<IActionResult> AddStock([FromBody] Inventory inventory)
        {
            if (inventory == null || inventory.Stock <= 0)
            {
                return BadRequest("Invalid inventory data.");
            }

            bool success = await _inventoryService.AddStock(inventory.ProductId, inventory.Stock);
            if (success)
            {
                return Ok("Stock added successfully.");
            }
            return BadRequest("Failed to add stock.");
        }

        // GET: api/inventory/{productId}
        [HttpGet("{productId}")]
        public async Task<IActionResult> GetStock(string productId)
        {
            int stock = await _inventoryService.GetStock(productId);
            return Ok(new { ProductId = productId, Stock = stock });
        }
    }
}
