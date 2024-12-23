CREATE TABLE Customer(
   id int not null primary key auto_increment,
   name varchar(255) not null,
   email varchar(255) not null unique,
   status varchar(255) not null
);