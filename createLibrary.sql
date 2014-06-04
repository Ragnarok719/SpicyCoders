CREATE TABLE BookType (
	typeName varchar(30),
	maxReservation int,
	overdueFee int,
	PRIMARY KEY (typeName)
);

CREATE TABLE Author (
	name varchar(20),
	PRIMARY KEY (name)
);

CREATE TABLE Publisher (
	name varchar(30),
	PRIMARY KEY (name)
);

CREATE TABLE SearchGenre (
	name varchar(20),
	PRIMARY KEY (name)
);

CREATE TABLE Patron (
	cardNumber int AUTO_INCREMENT,
	name varchar(20),
	phone int,
	address varchar(100), 
	unpaidFees int,
	PRIMARY KEY (cardNumber)
);


CREATE TABLE Librarian (
	idNumber int AUTO_INCREMENT, 
	name varchar(20), 
	address varchar(100),
	PRIMARY KEY (idNumber)
);

CREATE TABLE Category (
	name varchar(20),
	idNumber int AUTO_INCREMENT,
	superCategoryId int,
	PRIMARY KEY (idNumber),
	FOREIGN KEY (superCategoryId) REFERENCES Category (idNumber)
);

CREATE TABLE Book (
	isbn bigint,
	title varchar(100),
	description varchar(255), 
	currentQuantity int, 
	totalQuantity int, 
	publisherYear int,
	idNumber int NOT NULL, 
	typeName varchar(30) NOT NULL,
	PRIMARY KEY (isbn),
	FOREIGN KEY (idNumber) REFERENCES Category(idNumber),
	FOREIGN KEY (typeName) REFERENCES BookType(typeName)
);

CREATE TABLE HasSearchGenre (
	isbn bigint, 
	name varchar(20),
	PRIMARY KEY (isbn, name),
	FOREIGN KEY (isbn) REFERENCES Book(isbn),
	FOREIGN KEY (name) REFERENCES SearchGenre(name)
);

CREATE TABLE HasAuthor (
	isbn bigint, 
	name varchar(20),
	PRIMARY KEY (isbn, name),
	FOREIGN KEY (isbn) REFERENCES Book(isbn),
	FOREIGN KEY (name) REFERENCES Author(name)
);

CREATE TABLE HasPublisher (
	isbn bigint, 
	name varchar(30),
	PRIMARY KEY (isbn, name),
	FOREIGN KEY (isbn) REFERENCES Book(isbn),
	FOREIGN KEY (name) REFERENCES Publisher(name)
);

CREATE TABLE CheckOut (
	isbn bigint, 
	start timestamp, 
	end timestamp, 
	cardNumber int, 
	idNumber int,
	PRIMARY KEY (isbn, start, end, cardNumber, idNumber),
	FOREIGN KEY (isbn) REFERENCES Book(isbn),
	FOREIGN KEY (cardNumber) REFERENCES Patron(cardNumber),
	FOREIGN KEY (idNumber) REFERENCES Librarian(idNumber)
);

CREATE TABLE Returns (
	isbn bigint, 
	start timestamp, 
	end timestamp, 
	cardNumber int,
	returned timestamp, 
	checkoutId int,
	returnId int,
	PRIMARY KEY (isbn, start, end, cardNumber, checkoutId, returnId),
	FOREIGN KEY (isbn) REFERENCES Book(isbn),
	/*FOREIGN KEY (start, end) REFERENCES CheckOut(start, end),*/
	FOREIGN KEY (cardNumber) REFERENCES Patron(cardNumber),
	FOREIGN KEY (checkoutId) REFERENCES Librarian(idNumber),
	FOREIGN KEY (returnId) REFERENCES Librarian(idNumber)
);

