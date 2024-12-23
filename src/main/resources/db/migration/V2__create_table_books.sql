CREATE TABLE Book(
  id int not null primary key auto_increment,
  name varchar(255) not null,
  price decimal(10,2) not null,
  status varchar(255) not null,
  customer_id int not null,
  foreign key (customer_id) references Customer(id)

)