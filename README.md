**General description of the application**: <br>  
A web-based application that helps companies to deliver goods to their customers.<br>    

**Application requirements**:<br>

A) Completed:<br>
- Each courier has a list of addresses to visit for deliveries.
- Managers and admins can edit the list of addresses and couriers in the database.
- Couriers can generate the shortest route to optimize deliveries.
- After generating a delivery route, couriers can save it as a text file.
- Admins can upload files containing routes to the database, download them, or delete them.
- Every user role (courier, customer, manager, and admin) requires a username and password.
- The application provides a customer registration process.
- The application validates entered fields and handles errors.
- The weather forecast for the entered location is available to all users through the OpenWeatherMap API.
- Customer-entered addresses are validated using the Google Address Validation API.
- Customers can save validated addresses to the database for further company processing.

B) Planned:<br>
- All planned features have been added<br>

C) To-do list:<br>
- All planned updates have been made<br>

**Installation info**:
- Get your free Google API key: https://developers.google.com/maps/documentation/geocoding/get-api-key <br>
- Get your free OpenWeatherMap API key: https://openweathermap.org/appid <br>
- Download and unzip the application <br>
- Navigate to the project folder <br>
- Create a "secret.properties" file at: src/main/resources/ <br>
- Add the following content to the file: <br>
  google.api.key=your_real_google_api_key_here <br>
  weather.api.key=your_real_openweathermap_api_key_here <br>
- Return to the root project folder where docker-compose.yml is located <br>
- Run the console command: docker-compose up --build <br>
- Wait for the process to complete (this may take a few minutes depending on your system and network speed) <br>
- Once completed, the following link will appear in the console: http://localhost:8080 <br>
- Open the link in your browser to access the application <br>