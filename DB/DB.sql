CREATE DATABASE ClothesShop;

USE ClothesShop;

CREATE TABLE Users (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  firstname NVARCHAR(30) NOT NULL,
  lastname NVARCHAR(30) NOT NULL,
  email NVARCHAR(50) NOT NULL,
  avatar VARCHAR(200) NOT NULL,
  username VARCHAR(30) NOT NULL,
  password VARCHAR(64) NOT NULL,
  address NVARCHAR(200) NOT NULL,
  phone NVARCHAR(15) NOT NULL,
  roleid INT NOT NULL,
  status BIT NOT NULL
);

CREATE TABLE Types(
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name NVARCHAR(100)
);

CREATE TABLE Categories(
  categoryid INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  categoryname NVARCHAR(30),
  type_id INT,
  FOREIGN KEY (type_id) REFERENCES Types(id)
);

CREATE TABLE Suppliers(
  supplierid INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  suppliername NVARCHAR(100),
  supplierimage VARCHAR(255) NOT NULL
);

CREATE TABLE Products(
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  productname NVARCHAR(255) NOT NULL,
  supplierid INT,
  categoryid INT,
  size VARCHAR(40) NOT NULL,
  stock INT NOT NULL, 
  description NVARCHAR(255),
  images VARCHAR(255) NOT NULL,
  colors NVARCHAR(255) NOT NULL,
  releasedate DATE NOT NULL,
  discount FLOAT,
  unitSold INT,
  price DECIMAL(10,2) NOT NULL,
  status BIT NOT NULL,
  typeid INT,
  FOREIGN KEY (supplierid) REFERENCES Suppliers(supplierid) ON DELETE SET NULL ON UPDATE CASCADE,
  FOREIGN KEY (categoryid) REFERENCES Categories(categoryid) ON DELETE SET NULL ON UPDATE CASCADE,
  FOREIGN KEY (typeid) REFERENCES Types(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Payments(
  paymentid INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  payment_method NVARCHAR(30)
);

CREATE TABLE Orders(
  order_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  orderdate DATETIME,
  totalprice DECIMAL(10,2),
  paymentid INT NOT NULL,
  user_id int NOT NULL,
  status BIT NOT NULL,
  FOREIGN KEY (paymentid) REFERENCES Payments(paymentid),
  FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE OrderItem(
  order_item_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  quantity INT,
  price DECIMAL(10,2),
  product_id INT NOT NULL,
  order_id INT NOT NULL,
  FOREIGN KEY (product_id) REFERENCES Products(id) ON DELETE CASCADE,
  FOREIGN KEY (order_id) REFERENCES Orders(order_id)
);
