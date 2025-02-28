# **General Description of the Application**

A web-based application that helps companies deliver goods to their customers.

## **Key Features**

### **User Roles & Authentication**
- Supports Couriers, Customers, Managers, and Admins, each requiring a username and password.

### **Courier Route Management**
- Couriers receive a list of delivery addresses.
- Generate optimized delivery routes.
- Save routes as text files in the database.

### **Admin & Manager Controls**
- Managers and Admins can manage couriers and addresses.
- Only Admins can upload, download, and delete route files.

### **Customer Features**
- Register as a new Customer.
- Enter delivery addresses and validate via the Google Address Validation API.
- Submit validated addresses for company processing.

### **External API Integrations**
- Convert addresses to geographical coordinates using Google Geocoding API.
- Retrieve real-time weather data via OpenWeatherMap API.

## **Installation Info**

1. Get your free Google API key: [Google Geocoding API](https://developers.google.com/maps/documentation/geocoding/get-api-key)
2. Get your free OpenWeatherMap API key: [OpenWeatherMap API](https://openweathermap.org/appid)
3. Download and unzip the application.
4. Navigate to the project folder.
5. Create a `secret.properties` file at: `src/main/resources/`
6. Add the following content to the file:
   ```properties
   google.api.key=your_real_google_api_key_here
   weather.api.key=your_real_OpenWeatherMap_api_key_here
   ```
7. Return to the root project folder where `docker-compose.yml` is located.
8. Run the console command:
   ```sh
   docker-compose up --build
   ```
9. Wait for the process to complete (this may take a few minutes depending on your system and network speed).
10. Once completed, the following link will appear in the console:
    ```
    http://localhost:8080
    ```
11. Open the link in your browser to access the application.
