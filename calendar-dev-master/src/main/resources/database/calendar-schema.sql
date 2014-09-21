create database if not exists `calendar`;
use `calendar`;

drop table if exists `calendar_users`;
create table `calendar_users` (
    `id` int(32) unsigned not null auto_increment,
    `email` varchar(255) not null unique,
    `password` varchar(255) not null,
    `name` varchar(255) not null,
    PRIMARY KEY (`id`)    
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

drop table if exists `events`;
create table `events` (
    `id` int(32) unsigned not null auto_increment,
    `when` timestamp not null,
    `summary` varchar(255) not null,
    `description` varchar(500) not null,
    `owner` int(32) unsigned not null,
    `attendee` int(32) unsigned not null,
    PRIMARY KEY (`id`),
    KEY `fk_events_owner` (`owner`),
    KEY `fk_events_attendee` (`attendee`),
    CONSTRAINT `constraints_events_owner` FOREIGN KEY (`owner`) REFERENCES `calendar_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `constraints_events_attendee` FOREIGN KEY (`attendee`) REFERENCES `calendar_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;

insert into calendar_users(`id`,`email`,`password`,`name`) values (1,'user1@example.com','user1','User1');
insert into calendar_users(`id`,`email`,`password`,`name`) values (2,'admin1@example.com','admin1','Admin');
insert into calendar_users(`id`,`email`,`password`,`name`) values (3,'user2@example.com','user2','User1');

insert into events (`id`,`when`,`summary`,`description`,`owner`,`attendee`) values (100,'2013-10-04 20:30:00','Birthday Party','This is going to be a great birthday',1,2);
insert into events (`id`,`when`,`summary`,`description`,`owner`,`attendee`) values (101,'2013-12-23 13:00:00','Conference Call','Call with the client',3,1);
insert into events (`id`,`when`,`summary`,`description`,`owner`,`attendee`) values (102,'2014-01-23 11:30:00','Lunch','Eating lunch together',2,3);