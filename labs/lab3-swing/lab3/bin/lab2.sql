-- Delete the tables if they exist.
-- Disable foreign key checks, so the tables can 
-- be dropped in arbitrary order.

PRAGMA foreign_keys=OFF;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS performances;
DROP TABLE IF EXISTS movies;
DROP TABLE IF EXISTS theaters;
DROP TABLE IF EXISTS reservations;
PRAGMA foreign_keys=ON;


-- CREATE THE TABLES
CREATE TABLE movies(
	title TEXT NOT NULL,
	PRIMARY KEY  (title)
);

CREATE TABLE theaters(
	name TEXT not null,
	PRIMARY KEY  (name)
);

CREATE TABLE users (
	u_name   TEXT not null,
	u_nbr    TEXT not null,
	u_adr    TEXT,
	PRIMARY KEY (u_name)
);

CREATE TABLE performances(
	movie		  	TEXT not null,
	showdate	   	DATE,
	theater 		TEXT,
	nbr_seats   	INTE,
	PRIMARY KEY (movie, showdate),
	FOREIGN KEY (theater) REFERENCES theaters(name)
);

CREATE TABLE reservations( 
    reservation_id INTEGER,
    user_id TEXT,
    showdate DATE,
    movie_title TEXT,
    PRIMARY KEY (reservation_id), 
    FOREIGN KEY (user_id) REFERENCES users(u_name),
    FOREIGN KEY (showdate, movie_title) REFERENCES performances(showdate, movie)
);

-- Insert data into the tables.
INSERT
INTO theaters(name)
VALUES ('Storgatan'),
       ('Norr');

INSERT
INTO movies(title)
VALUES ('Star Wars'),
       ('Solsidan'),
       ('Call Me By Your Name');

INSERT 
INTO    users (u_name, u_nbr, u_adr) 
VALUES  ('tiger82', '55636', 'Storgatan 57'), 
	('lejon52','234234',null),
	('jaguar42','23423423', 'Amazongatan 3');

INSERT 
INTO    performances(movie, showdate, theater, nbr_seats) 
VALUES  ('Star Wars', '2018-01-08', 'Storgatan', 170), 
	('Solsidan','2018-01-08','Storgatan', 130),
	('Call Me By Your Name','2018-01-11', 'Norr', 150),
	('Call Me By Your Name','2018-01-12', 'Norr', 180),
	('Star Wars','2018-01-10', 'Norr',200);

