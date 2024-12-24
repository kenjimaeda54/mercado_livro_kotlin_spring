create table Customer_Roles (
  customer_id int not null,
  role varchar(50) not null,
  foreign key(customer_id) references Customer(id)
);