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
- Create a file "secret.properties" on this path: src/main/resources/ <br>  
- The file's content has to be: <br>
google.api.key=here should be your real Google api key <br>
weather.api.key=here should be your real OpenWeatherMap api key <br>  
- If you run the app without Docker in an IDE, use the SQL scripts to create and fill in the database from here: src/main/resources/sql-files/init.sql