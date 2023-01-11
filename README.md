# Chat Server


#### About the project:
This is a chat server providing an HTTP API for managing user chats and messages.

#### Preparations and requirements:
You'll need to install Tomcat Server; create a database and all necessary tables with MySQL (find the instructions for that in the end of this document); open this project with your IDE, start Tomcat Server and use Postman for sending HTTP requests.

#### Main API methods:
###### Add a new user
request POST
data '{"username": "user_1"}'
http://localhost:8080/users
Expected answer: id of a newly created user or an HTTP error code with problem discription
###### Start a new chat for the users
request POST
data '{"name": "chat_1", "users": ["<USER_1>", "<USER_2>, etc"]}'
http://localhost:8080/chats
Expected answer: id of a newly created chat or an HTTP error code with problem discription
###### Add a new message by a user in a chat
request POST
data '{"chat": "<CHAT_1>", "author": "<USER_1>", "text": "text"}'
http://localhost:8080/messages
Expected answer: id of a newly created message or an HTTP error code with problem discription
###### Get a list of chats for a user
request POST
data '{"user": "<USER_1>"}'
http://localhost:8080/chats/get_by_user
Expected answer: list of all user's chats with all fields filled, sorted by chat creation time or an HTTP error code with problem discription
###### Get a list of messages in a chat
request POST
data '{"chat": "<CHAT_1>"}'
http://localhost:8080/messages/get_by_chat
Expected answer: list of all chat's messages with all fields filled, sorted by message creation time or an HTTP error code with problem discription

#### SQL queries for configuring the database:
CREATE DATABASE backend_task_internship;
USE backend_task_internship;

CREATE TABLE backend_task_internship.users (
id int NOT NULL AUTO_INCREMENT,
username varchar(30) UNIQUE NOT NULL,
created_at DATETIME NOT NULL,
PRIMARY KEY (id)
);

CREATE TABLE backend_task_internship.chats (
id int NOT NULL AUTO_INCREMENT,
chat_name varchar(30) UNIQUE NOT NULL,
created_at DATETIME NOT NULL,
PRIMARY KEY (id)
);

CREATE TABLE backend_task_internship.user_chat (
user_id int NOT NULL,
chat_id int NOT NULL,
PRIMARY KEY (user_id, chat_id),
FOREIGN KEY (user_id) REFERENCES backend_task_internship.users(id),
FOREIGN KEY (chat_id) REFERENCES backend_task_internship.chats(id));

CREATE TABLE backend_task_internship.messages (
id int NOT NULL AUTO_INCREMENT,
chat_id int NOT NULL,
author_id int NOT NULL,	
text_of_message TINYTEXT,
created_at DATETIME NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (chat_id) REFERENCES backend_task_internship.chats(id),
FOREIGN KEY (author_id) REFERENCES backend_task_internship.users(id)
);


