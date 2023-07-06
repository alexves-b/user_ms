CREATE TABLE users (
   id INT NOT NULL,
   user_name VARCHAR(50) NOT NULL,
   email VARCHAR(20) NOT NULL,
   registration_date DATE
);

insert into users(id,user_name,email,registration_date) values (1,'user_1','alexves@bk.ru','2022-01-01'),
(2,'user_2','vasya@bk.ru','2022-05-01'),
(3,'user_3','masha@bk.ru','2022-08-03');