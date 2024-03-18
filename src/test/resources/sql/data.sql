insert into courier(first_name, last_name, phone)
values ('Ivan', 'Ivanov', '+71234567891'),
       ('Oleg', 'Olegov', '+71234567892'),
       ('Sidor', 'Sidorov', '+71234567893'),
       ('Petr', 'Petrov', '+71234567894'),
       ('Igor', 'Igorev', '+71234567895');

insert into address(country_name, city_name, street_name, house_number, courier_id)
values ('Russia', 'Moscow', 'Main street', '11', '1'),
       ('France', 'Paris', 'Side street', '12', '2'),
       ('England', 'London', 'Back street', '13', '3'),
       ('Germany', 'Berlin', 'Down street', '14', '4'),
       ('Italy', 'Rome', 'Up street', '15', '1');

insert into users (first_name, last_name, email, username, password, enabled)
values ('Alex', 'Smith', 'admin@server.com', 'admin', '{bcrypt}$2a$12$Ar7GX6ecZR4GYCVRaBdIwe3VdovgTWy8JQ3UbEk4fju1tXoyvSG7C', '1'),
       ('Mary', 'Sue', 'manager@server.com', 'manager', '{bcrypt}$2a$12$Ar7GX6ecZR4GYCVRaBdIwe3VdovgTWy8JQ3UbEk4fju1tXoyvSG7C', '1'),
       ('Harry', 'Potter', 'harry@server.com', 'courier1', '{bcrypt}$2a$12$Ar7GX6ecZR4GYCVRaBdIwe3VdovgTWy8JQ3UbEk4fju1tXoyvSG7C', '1'),
       ('Frodo', 'Baggins', 'frodo@server.com', 'courier2', '{bcrypt}$2a$12$Ar7GX6ecZR4GYCVRaBdIwe3VdovgTWy8JQ3UbEk4fju1tXoyvSG7C', '1'),
       ('Tom', 'Sawyer', 'tom@server.com', 'customer1', '{bcrypt}$2a$12$Ar7GX6ecZR4GYCVRaBdIwe3VdovgTWy8JQ3UbEk4fju1tXoyvSG7C', '1');

insert into authorities (username, authority, user_id)
values ('admin', 'ROLE_ADMIN', '1'),
       ('manager', 'ROLE_MANAGER', '2'),
       ('courier1', 'ROLE_COURIER', '3'),
       ('courier2', 'ROLE_COURIER', '4'),
       ('customer1', 'ROLE_CUSTOMER', '5');