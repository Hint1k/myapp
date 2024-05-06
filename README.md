General description of the application: 
A web based application that helps a company to deliver goods to its customers.  

Application requirements:

A) Completed:
1) Every courier has an individual task. It consists of list of addresses that has to be visited to complete the delivery.
2) The manager/admin can edit the list of addresses and the list of couriers in the database.
3) The couriers can only see their individual tasks, but cannot edit anything. 
4) However, the couriers can generate the shortest route to speed up the delivery.
5) Every courier, manager and admin should have a username and a password to access the application.
6) The application has to check the validity of the fields (fields are not empty etc.).
7) The application shows the weather forecast to anyone. This info is taken from a weather forecast site based on the location entered by user.
8) The application has to have a new user registration process.
9) After a courier generated the delivery route, it can be saved as a text file right from the calculation page.
10) The route as a text file can be uploaded to database, downloaded or deleted from database only by admin.
11) The application has to check the validity of the address entered by a customer using a Google Address Validation API.
12) The customer can save the validated address to database for further processing by company.  
 
B) Planned:
- All planned features have been added

C) To-do list:
- Handles errors for all classes
- Update all tests after that

Installation info:
1) Create file "secret.properties" on this path: src/main/resources/
2) The file's content has to be:  
google.api.key=here should be your real Google api key  
weather.api.key=here should be your real OpenWeatherMap api key
3) SQL scripts to create and fill in the database are here: src/main/resources/sql-files/init.sql