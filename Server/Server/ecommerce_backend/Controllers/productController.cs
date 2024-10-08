using Microsoft.AspNetCore.Mvc;
using WebServerWithMongoDB.Services;
using MongoDB.Driver;
using WebServerWithMongoDB.Models;
[ApiController]
[Route("api/[controller]")]
public class ProductController : ControllerBase
{
    private readonly ProductService _productService;

    public ProductController(ProductService productService)
    {
        _productService = productService;
    }

[HttpGet("{productId}")]
public async Task<IActionResult> GetProductById(string productId)
{
    Console.WriteLine($"GetProductById called with ID: {productId}");
    var product = await _productService.GetProductById(productId);
    if (product != null)
        return Ok(product);
    return NotFound("Product not found.");
}


    [HttpGet]
public async Task<IActionResult> GetAllProducts()
{
    Console.WriteLine("GetAllProducts API called.");
    var products = await _productService.GetAllProducts();
    return Ok(products);
}


    [HttpPost("create")]
    public async Task<IActionResult> CreateProduct([FromBody] Product product)
    {
        await _productService.CreateProduct(product);
        return Ok("Product created successfully");
    }

    [HttpPut("update/{productId}")]
    public async Task<IActionResult> UpdateProduct(string productId, [FromBody] Product product)
    {
        await _productService.UpdateProduct(productId, product);
        return Ok("Product updated successfully");
    }

    [HttpDelete("delete/{productId}")]
    public async Task<IActionResult> DeleteProduct(string productId)
    {
        await _productService.DeleteProduct(productId);
        return Ok("Product deleted successfully");
    }

    [HttpPost("activate/{productId}")]
    public async Task<IActionResult> ActivateProduct(string productId)
    {
        var success = await _productService.ActivateProduct(productId);
        if (success)
            return Ok("Product activated successfully.");
        return NotFound("Product not found.");
    }

    [HttpPost("deactivate/{productId}")]
    public async Task<IActionResult> DeactivateProduct(string productId)
    {
        var success = await _productService.DeactivateProduct(productId);
        if (success)
            return Ok("Product deactivated successfully.");
        return NotFound("Product not found.");
    }
}
