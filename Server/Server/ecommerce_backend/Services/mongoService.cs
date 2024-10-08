using MongoDB.Driver;
using WebServerWithMongoDB.Models;

namespace WebServerWithMongoDB.Services
{
    public class MongoService
    {
        private readonly IMongoDatabase _database;

        public MongoService(IMongoDBSettings settings)
        {
            var client = new MongoClient(settings.ConnectionString);
            _database = client.GetDatabase(settings.DatabaseName);
        }

        // Example: Get a collection from the MongoDB database
        // public IMongoCollection<admin> GetCollection()
        // {
        //     return _database.GetCollection<admin>("admin");
        // }
          public IMongoCollection<Admin> GetCollection<Admin>() // Ensure you are using Admin as a generic type here
        {
            return _database.GetCollection<Admin>("admin"); // Replace "admin" with your actual collection name
        }
    }
}
