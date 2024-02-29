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