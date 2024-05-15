**General description of the application**:  
A web-based application that helps companies deliver goods to their customers.    

**Application requirements**:  

A) Completed:  
1) *Individual Tasks*: Each courier has an individual task consisting of a list of addresses that must be visited to complete deliveries.  
2) *Task Management*: The manager/admin can edit the list of addresses and couriers in the database.  
3) *Courier Access*: Couriers can only see their individual tasks and cannot edit anything.  
4) *Route Optimization*: However, couriers can generate the shortest route to expedite deliveries.  
5) *Secure Login*: Every courier, customer, manager, and admin requires a username and password to access the application.  
6) *Data Validation*: The application must check the validity of fields (ensuring they are not empty, etc.).  
7) *Weather Integration*: The application displays the weather forecast for any user based on their entered location (information retrieved from a weather forecast website).  
8) *Route Management*: After generating a delivery route, couriers can save it as a text file directly from the calculation page.  
9) *Route Administration*: Only admins can upload route text files to the database, download them, or delete them.  
10) *Customer Registration*: The application provides a new customer registration process.  
11) *Address Validation*: The application verifies the validity of customer-entered addresses using the Google Address Validation API.  
12) *Customer Address Storage*: Customers can save validated addresses to the database for further company processing.   
 
B) Planned:  
- All planned features have been added

C) To-do list:  
- All planned updates have been made

**Installation info**:
1) Create file "secret.properties" on this path: src/main/resources/  
2) The file's content has to be:    
google.api.key=here should be your real Google api key    
weather.api.key=here should be your real OpenWeatherMap api key  
3) SQL scripts to create and fill in the database are here: src/main/resources/sql-files/init.sql