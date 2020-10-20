CREATE DATABASE simpletodo;

CREATE SCHEMA tasks;

CREATE TABLE tasks.container
(
  id serial NOT NULL PRIMARY KEY,
  title text NOT NULL
);

CREATE TABLE tasks.card
(
  id serial NOT NULL PRIMARY KEY ,
  title text NOT NULL,
  id_container int REFERENCES tasks.container (id)
);