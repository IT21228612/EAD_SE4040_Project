using Microsoft.Extensions.Options;
using MongoDB.Driver;
using WebServerWithMongoDB.Models;
using WebServerWithMongoDB.Services;

var builder = WebApplication.CreateBuilder(args);

// Bind MongoDB settings from appsettings.json
builder.Services.Configure<MongoDBSettings>(
    builder.Configuration.GetSection(nameof(MongoDBSettings))
);

// Register IMongoDBSettings as a singleton
builder.Services.AddSingleton<IMongoDBSettings>(sp =>
    sp.GetRequiredService<IOptions<MongoDBSettings>>().Value
);

// Register MongoDB Client and Database
builder.Services.AddSingleton<IMongoClient>(sp =>
{
    var settings = sp.GetRequiredService<IOptions<MongoDBSettings>>().Value;
    return new MongoClient(settings.ConnectionString);  
});

builder.Services.AddSingleton(sp =>
{
    var settings = sp.GetRequiredService<IOptions<MongoDBSettings>>().Value;
    var client = sp.GetRequiredService<IMongoClient>();
    return client.GetDatabase(settings.DatabaseName);
});

// Register the MongoService as a singleton
builder.Services.AddSingleton<MongoService>();

// Register Services as a scoped service
builder.Services.AddScoped<ProductService>();
builder.Services.AddScoped<OrderService>();

// Add Swagger services
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

// Add controller services
builder.Services.AddControllers();

var app = builder.Build(); // Build happens after service registration

// Enable Swagger UI in development mode
if (app.Environment.IsDevelopment())
{
    app.UseDeveloperExceptionPage();
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();
app.UseRouting();
app.UseAuthorization();

// Configure the route endpoints
app.MapControllers();

app.Run();
