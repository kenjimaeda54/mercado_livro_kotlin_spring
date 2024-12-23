CREATE TABLE Purchase(
  id int not null primary key auto_increment,
  price decimal(10,2) not null,
  create_at datetime not null,
  nfe varchar(255),
  customer_id int not null,
  foreign key (customer_id) references Customer(id)

);

CREATE TABLE Purchase_Book(
  purchase_id int not null,
  book_id int not null,
  foreign key (purchase_id) references Purchase(id),
  foreign key (book_id) references Book(id),
  primary key(purchase_id,book_id)
);