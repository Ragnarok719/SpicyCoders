# Create all the tables

CREATE TABLE IF NOT EXISTS BookType (
	typeName varchar(30),
	maxReservation int,
	overdueFee int,
	PRIMARY KEY (typeName)
);

CREATE TABLE IF NOT EXISTS Author (
	name varchar(20),
	PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS Publisher (
	name varchar(30),
	PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS SearchGenre (
	name varchar(20),
	PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS Patron (
	cardNumber int AUTO_INCREMENT,
	name varchar(20),
	phone int,
	address varchar(100), 
	unpaidFees int,
	PRIMARY KEY (cardNumber)
) AUTO_INCREMENT = 100000;


CREATE TABLE IF NOT EXISTS Librarian (
	idNumber int AUTO_INCREMENT, 
	name varchar(20), 
	address varchar(100),
	PRIMARY KEY (idNumber)
) AUTO_INCREMENT = 100000;

CREATE TABLE IF NOT EXISTS Category (
	name varchar(20),
	idNumber int AUTO_INCREMENT,
	superCategoryId int,
	PRIMARY KEY (idNumber),
	FOREIGN KEY (superCategoryId) REFERENCES Category (idNumber)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Book (
	isbn bigint,
	title varchar(100),
	description varchar(255), 
	currentQuantity int, 
	totalQuantity int, 
	publisherYear int,
	idNumber int NOT NULL, 
	typeName varchar(30) NOT NULL,
	PRIMARY KEY (isbn),
	FOREIGN KEY (idNumber) REFERENCES Category(idNumber)
	ON DELETE CASCADE
	ON UPDATE CASCADE,
	FOREIGN KEY (typeName) REFERENCES BookType(typeName)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS HasSearchGenre (
	isbn bigint, 
	name varchar(20),
	PRIMARY KEY (isbn, name),
	FOREIGN KEY (isbn) REFERENCES Book(isbn)
	ON DELETE CASCADE,
	FOREIGN KEY (name) REFERENCES SearchGenre(name)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS HasAuthor (
	isbn bigint, 
	name varchar(20),
	PRIMARY KEY (isbn, name),
	FOREIGN KEY (isbn) REFERENCES Book(isbn)
	ON DELETE CASCADE
	ON UPDATE CASCADE,
	FOREIGN KEY (name) REFERENCES Author(name)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS HasPublisher (
	isbn bigint, 
	name varchar(30),
	PRIMARY KEY (isbn, name),
	FOREIGN KEY (isbn) REFERENCES Book(isbn)
	ON DELETE CASCADE
	ON UPDATE CASCADE,
	FOREIGN KEY (name) REFERENCES Publisher(name)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS CheckOut (
	isbn bigint, 
	start timestamp, 
	end timestamp, 
	cardNumber int, 
	idNumber int,
	PRIMARY KEY (isbn, start, end, cardNumber, idNumber),
	FOREIGN KEY (isbn) REFERENCES Book(isbn)
	ON DELETE CASCADE
	ON UPDATE CASCADE,
	FOREIGN KEY (cardNumber) REFERENCES Patron(cardNumber)
	ON DELETE CASCADE
	ON UPDATE CASCADE,
	FOREIGN KEY (idNumber) REFERENCES Librarian(idNumber)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Returns (
	isbn bigint, 
	start timestamp, 
	end timestamp, 
	cardNumber int,
	returned timestamp, 
	checkoutId int,
	returnId int,
	PRIMARY KEY (isbn, start, end, cardNumber, checkoutId, returnId),
	FOREIGN KEY (isbn, start, end, cardNumber, checkoutId) REFERENCES CheckOut(isbn, start, end, cardNumber, idNumber)
	ON DELETE CASCADE
	ON UPDATE CASCADE,
	FOREIGN KEY (returnId) REFERENCES Librarian(idNumber)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);


# Populate tables

INSERT
INTO BookType(typeName, maxReservation, overdueFee)
VALUES ('Adult Fiction', '14', '1'),
('Adult Nonfiction', '7', '2'),
('Children\'s Fiction', '21', '1'),
('Children\'s Nonfiction', '21', '1'),
('Audiobooks', '14', '3');

INSERT
INTO Author
VALUES ('J.K. Rowling'),
('J.R.R Tolkien'),
('Johannes Gehrke'),
('Raghu Ramakrishnan'),
('Michael Harrington'),
('Scott Freeman'),
('Joan C. Sharp'),
('Mary Shelley'),
('Mark Twain'),
('Jane Austen'),
('Agatha Christie');

INSERT
INTO Publisher
VALUES ('Scholastic'),
('Scholastic Paperbacks'),
('Arthur A. Levine Books'),
('Houghton Mifflin Harcourt'),
('Mcgraw Hill Ryerson Ltd'),
('Pearson Education Canada'),
('Penguin Classics'),
('Harper Collins Publishers');

INSERT 
INTO SearchGenre
VALUES ('Fantasy'),
('Textbook'),
('Mystery'),
('Humor'),
('Science fiction'),
('Biography'),
('Essay'),
('Reference book');

INSERT
INTO Patron(name, phone, address, unpaidFees)
VALUES ('Jack Smith', '5555555', '1234 Madeup St', '0'),
('Jane Lambda', '1234567', '9876 Weird Ave', '3'),
('Bill Chan', '1235354', '3141 Pi Rd', '10'),
('Person Name', '1129284', '1038 Random Rd', '0'),
('Norman Doorman', '3072846', '1203 Knob Street', '35');


INSERT
INTO Librarian(name, address)
VALUES ('Strict Shusher', '5000 Quiet Ave'),
('Nice Nora', '2734 Kindness Dr'),
('Clumsy Chad', '3756 Knockover Rd'),
('Lily Late', '1984 Sorry St'),
('Peter Pan', '4729 Neverland Dr');

INSERT
INTO Category(name, superCategoryId)
VALUES ('Computer Science', null),
('Canadian Literature', null),
('Biology', null),
('Databases', '1'),
('Genetics', '3'),
('Fiction', null);

INSERT
INTO Book(isbn, title, description, currentQuantity, totalQuantity, publisherYear, idNumber, typeName)
VALUES 
('9780590353427', 'Harry Potter and the Sorcerer\'s Stone', 'Harry Potter goes to Hogwarts.', '1', '3', '1999',
	(SELECT idNumber FROM Category WHERE name='Fiction'), (SELECT typeName FROM BookType WHERE typeName='Children\'s Fiction')),
('9780439064873', 'Harry Potter and the Chamber of Secrets', 'Harry Potter fights a basilisk.', '2', '2', '2000',
	(SELECT idNumber FROM Category WHERE name='Fiction'), (SELECT typeName FROM BookType WHERE typeName='Children\'s Fiction')),
('9780545139700', 'Harry Potter and the Deathly Hallows', 'The final Harry Potter book.', '0', '2', '2009',
	(SELECT idNumber FROM Category WHERE name='Fiction'), (SELECT typeName FROM BookType WHERE typeName='Children\'s Fiction')),
('9780547928227', 'The Hobbit', 'In a hole in the ground there lived a hobbit.', '2', '5', '2012',
	(SELECT idNumber FROM Category WHERE name='Fiction'), (SELECT typeName FROM BookType WHERE typeName='Children\'s Fiction')),
('9780072465631', 'Database Management Systems', 'CPSC 304 Textbook', '0', '1', '2003',
	(SELECT idNumber FROM Category WHERE name='Databases'), (SELECT typeName FROM BookType WHERE typeName='Adult Nonfiction')),
('9780321834843', 'Biological Science', 'First Year Biology Textbook', '0', '2', '2012',
	(SELECT idNumber FROM Category WHERE name='Biology'), (SELECT typeName FROM BookType WHERE typeName='Adult Nonfiction')),
('9780141439471', 'Frankenstein', 'The classic about a monster creating life.', '3', '3', '2003', 
	(SELECT idNumber FROM Category WHERE name='Fiction'), (SELECT typeName FROM BookType WHERE typeName='Adult Fiction'));

INSERT
INTO HasSearchGenre(isbn, name)
VALUES 
((SELECT isbn FROM Book WHERE title='Harry Potter and the Sorcerer\'s Stone'), (SELECT name FROM SearchGenre WHERE name='Fantasy')),
((SELECT isbn FROM Book WHERE title='Harry Potter and the Chamber of Secrets'), (SELECT name FROM SearchGenre WHERE name='Fantasy')),
((SELECT isbn FROM Book WHERE title='Harry Potter and the Deathly Hallows'), (SELECT name FROM SearchGenre WHERE name='Fantasy')),
((SELECT isbn FROM Book WHERE title='The Hobbit'), (SELECT name FROM SearchGenre WHERE name='Fantasy')),
((SELECT isbn FROM Book WHERE title='Database Management Systems'), (SELECT name FROM SearchGenre WHERE name='Textbook')),
((SELECT isbn FROM Book WHERE title='Biological Science'), (SELECT name FROM SearchGenre WHERE name='Textbook')),
((SELECT isbn FROM Book WHERE title='Frankenstein'), (SELECT name FROM SearchGenre WHERE name='Science Fiction'));


INSERT
INTO HasAuthor(isbn, name)
VALUES 
((SELECT isbn FROM Book WHERE title='Harry Potter and the Sorcerer\'s Stone'), (SELECT name FROM Author WHERE name='J.K. Rowling')),
((SELECT isbn FROM Book WHERE title='Harry Potter and the Chamber of Secrets'), (SELECT name FROM Author WHERE name='J.K. Rowling')),
((SELECT isbn FROM Book WHERE title='Harry Potter and the Deathly Hallows'), (SELECT name FROM Author WHERE name='J.K. Rowling')),
((SELECT isbn FROM Book WHERE title='The Hobbit'), (SELECT name FROM Author WHERE name='J.R.R Tolkien')),
((SELECT isbn FROM Book WHERE title='Database Management Systems'), (SELECT name FROM Author WHERE name='Johannes Gehrke')),
((SELECT isbn FROM Book WHERE title='Database Management Systems'), (SELECT name FROM Author WHERE name='Raghu Ramakrishnan')),
((SELECT isbn FROM Book WHERE title='Biological Science'), (SELECT name FROM Author WHERE name='Scott Freeman')),
((SELECT isbn FROM Book WHERE title='Biological Science'), (SELECT name FROM Author WHERE name='Michael Harrington')),
((SELECT isbn FROM Book WHERE title='Biological Science'), (SELECT name FROM Author WHERE name='Joan C. Sharp')),
((SELECT isbn FROM Book WHERE title='Frankenstein'), (SELECT name FROM Author WHERE name='Mary Shelley'));

INSERT
INTO HasPublisher(isbn, name)
VALUES 
((SELECT isbn FROM Book WHERE title='Harry Potter and the Sorcerer\'s Stone'), (SELECT name FROM Publisher WHERE name='Scholastic')),
((SELECT isbn FROM Book WHERE title='Harry Potter and the Chamber of Secrets'), (SELECT name FROM Publisher WHERE name='Scholastic Paperbacks')),
((SELECT isbn FROM Book WHERE title='Harry Potter and the Deathly Hallows'), (SELECT name FROM Publisher WHERE name='Arthur A. Levine Books')),
((SELECT isbn FROM Book WHERE title='The Hobbit'), (SELECT name FROM Publisher WHERE name='Houghton Mifflin Harcourt')),
((SELECT isbn FROM Book WHERE title='Database Management Systems'), (SELECT name FROM Publisher WHERE name='Mcgraw Hill Ryerson Ltd')),
((SELECT isbn FROM Book WHERE title='Biological Science'), (SELECT name FROM Publisher WHERE name='Pearson Education Canada')),
((SELECT isbn FROM Book WHERE title='Frankenstein'), (SELECT name FROM Publisher WHERE name='Penguin Classics'));

INSERT
INTO Checkout(isbn, start, end, cardNumber, idNumber)
VALUES
((SELECT isbn FROM Book WHERE title='Harry Potter and the Chamber of Secrets'), '2014-5-3 10:31:10.75', '2014-5-24 10:31:10.75',
	(SELECT cardNumber FROM Patron WHERE name='Jack Smith'), (SELECT idNumber FROM Librarian WHERE name='Nice Nora')),
((SELECT isbn FROM Book WHERE title='Biological Science'), '2014-4-30 12:45:26.36', '2014-5-6 12:45:26.36',
	(SELECT cardNumber FROM Patron WHERE name='Bill Chan'), (SELECT idNumber FROM Librarian WHERE name='Strict Shusher')),
((SELECT isbn FROM Book WHERE title='Database Management Systems'), '2014-4-15 08:21:14.28', '2014-4-22 08:21:14.28',
	(SELECT cardNumber FROM Patron WHERE name='Jack Smith'), (SELECT idNumber FROM Librarian WHERE name='Nice Nora')),
((SELECT isbn FROM Book WHERE title='The Hobbit'), '2014-5-3 12:31:11.75', '2014-5-24 12:31:11.75',
	(SELECT cardNumber FROM Patron WHERE name='Person Name'), (SELECT idNumber FROM Librarian WHERE name='Clumsy Chad')),
((SELECT isbn FROM Book WHERE title='The Hobbit'), '2014-4-5 09:21:14.28', '2014-4-26 09:21:14.28',
	(SELECT cardNumber FROM Patron WHERE name='Norman Doorman'), (SELECT idNumber FROM Librarian WHERE name='Lily Late')),
((SELECT isbn FROM Book WHERE title='Biological Science'), '2014-5-30 10:47:50.13', '2014-5-6 10:47:50.13',
	(SELECT cardNumber FROM Patron WHERE name='Jane Lambda'), (SELECT idNumber FROM Librarian WHERE name='Peter Pan'));


INSERT 
INTO Returns (isbn, start, end, cardNumber, checkoutId, returned, returnID)
SELECT isbn, start, end, cardNumber, idNumber, '2014-5-20 12:28:13.34', 
(SELECT idNumber FROM Librarian WHERE name='Strict Shusher') FROM Checkout where start = '2014-05-03 10:31:11';

INSERT 
INTO Returns (isbn, start, end, cardNumber, checkoutId, returned, returnID)
SELECT isbn, start, end, cardNumber, idNumber, '2014-5-6 12:44:13.34', 
(SELECT idNumber FROM Librarian WHERE name='Nice Nora') FROM Checkout where start = '2014-04-30 12:45:26';

INSERT 
INTO Returns (isbn, start, end, cardNumber, checkoutId, returned, returnID)
SELECT isbn, start, end, cardNumber, idNumber, '2014-4-20 09:12:54.23', 
(SELECT idNumber FROM Librarian WHERE name='Clumsy Chad') FROM Checkout where start = '2014-04-15 08:21:14';

INSERT 
INTO Returns (isbn, start, end, cardNumber, checkoutId, returned, returnID)
SELECT isbn, start, end, cardNumber, idNumber, '2014-5-9 11:56:43.27', 
(SELECT idNumber FROM Librarian WHERE name='Peter Pan') FROM Checkout where start = '2014-05-03 12:31:12';

INSERT 
INTO Returns (isbn, start, end, cardNumber, checkoutId, returned, returnID)
SELECT isbn, start, end, cardNumber, idNumber, '2014-4-20 12:22:12.23', 
(SELECT idNumber FROM Librarian WHERE name='Peter Pan') FROM Checkout where start = '2014-04-05 09:21:14';



