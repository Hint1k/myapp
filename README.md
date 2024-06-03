<ins>General description of the application</ins>: <br>  
A web-based application that helps companies to deliver goods to their customers.<br>    

<ins>Application requirements</ins>:<br>

A) Completed:<br>
- Each courier should have a list of addresses that must be visited to complete deliveries.<br>
- The manager and admin can edit the list of addresses and list of couriers in the database.<br>
- Couriers can generate the shortest route to expedite deliveries.<br>
- After generating a delivery route, couriers can save it as a text file.<br>
- Admin can upload files with routes to the database, download them, or delete them. <br>
- Every courier, customer, manager and admin should have a username and password to access the application. <br>
- The application provides a new customer registration process.<br>
- The application must check the validity of fields and handle errors.<br>
- The weather forecast based on the entered location is available to anyone with the help of OpenWeatherMap API.<br>
- The customer-entered addresses are validated using the Google Address Validation API.<br>
- Customers can save validated addresses to the database for further processing by company.<br>

B) Planned:<br>
- All planned features have been added<br>

C) To-do list:<br>
- All planned updates have been made<br>

<ins>Installation info</ins>:
- Create file "secret.properties" on this path: src/main/resources/ <br>  
- The file's content has to be: <br>
google.api.key=here should be your real Google api key <br>
weather.api.key=here should be your real OpenWeatherMap api key <br>  
- If the app runs without Docker in IDE, then use SQL scripts to create and fill in the database from here: src/main/resources/sql-files/init.sql