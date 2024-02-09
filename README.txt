General description of application:
A web based application for couriers.
It helps couriers to delivers goods in accordance with their individual tasks.

Customer requirements:

A) Completed:
1) Each task consists of addresses that has to be visited to complete deliveries.
2) The manager/admin can edit the list of addresses and the list of couriers in the database.
3) The couriers can only see their individual tasks, but cannot edit anything.
4) However, the couriers can generate the shortest route to speed up the delivery.
5) Every courier, manager and admin should have a user name and a password
to access the application.
6) The application has to check the validity of the fields (fields are not empty etc)
7) The application shows the weather forecast for everyone. This info is taken
from a weather forecast site based on the location entered by user.

B) Still need to be done:
1) Test RouteCalculation and RouteController classes
2) Update endpoints in accordance with the REST naming convention
3) New user registration process
4) After a courier generated the route, it should be saved in database and
can be downloaded as a file by a manager or admin
5) Thymeleaf template to simulate a customer entering address to request the delivery
6) The application has to check the validity of the address entered by customer
on Google map, before recording it to the database.
7) Implement generic DAO and generic service classes.