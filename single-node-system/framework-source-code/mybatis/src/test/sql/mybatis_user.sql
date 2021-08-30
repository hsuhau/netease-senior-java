-- auto-generated definition
create table user
(
    id     int auto_increment
        primary key,
    name   varchar(10) not null,
    age    int         null,
    gender varchar(10) not null
);

INSERT INTO mybatis.user (id, name, age, gender) VALUES (1, 'hsuhau', 24, 'man');