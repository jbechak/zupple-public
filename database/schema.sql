begin transaction;

drop table if exists wordsearch_word;
drop table if exists crossword_wordclue;
drop table if exists wordsearch;
drop table if exists crossword;
drop table if exists sudoku;
drop table if exists account;
drop table if exists users;

CREATE TABLE users (
	user_id serial NOT NULL,
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	first_name varchar(50),
	last_name varchar(50),
	email varchar(100),

	CONSTRAINT pk_user_id PRIMARY KEY (user_id),
	CONSTRAINT uq_username UNIQUE (username)
);

--create table account (
--    user_id serial primary key,
--    username varchar(50) not null,
--    password_hash varchar(300) not null
--);

create table sudoku (
    id serial primary key,
    user_id integer references users (user_id),
    title varchar(50),
    show_title boolean,
    difficulty integer,
    show_difficulty boolean,
    instructions varchar(250),
    grid_string varchar(200)
);

create table crossword (
    id serial primary key,
    user_id integer references users (user_id),
    title varchar(60),
    description varchar(500),
    difficulty varchar(20),
    genre varchar(20),
    instructions varchar(500),
    width int not null check (width < 200),
    height int not null check (height < 200),
    grid_string varchar(4000)
);

create table wordsearch (
	id serial primary key,
	user_id integer references users (user_id),
	title varchar(60),
	description varchar(500),
	difficulty varchar(20),
	genre varchar(20),
    instructions varchar(500),
	width int not null check (width < 100),
	height int not null check (height < 100),
    grid_string varchar(4000)
);

create table crossword_wordclue (
    wordclue_id serial primary key,
    crossword_id integer references crossword (id),
    clue_direction varchar(8),
    clue_number integer,
    word varchar(30),
    clue varchar(200)
);

create table wordsearch_word (
    word_id serial primary key,
    wordsearch_id integer references wordsearch (id),
    word varchar(30)
);

--insert into account (username, password_hash)
--values ('Justin', 'password');
--
--insert into account (username, password_hash)
--values ('Melodi', 'password');
--
--insert into account (username, password_hash)
--values ('Canly', 'password');

insert into users (username, password_hash, role)
values ('Justin', 'password', 'admin');

insert into users (username, password_hash, role)
values ('Melodi', 'password', 'default');

insert into users (username, password_hash, role)
values ('Canly', 'password', 'default');

INSERT INTO users (username,password_hash,role) VALUES ('user','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('admin','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_ADMIN');




commit;
