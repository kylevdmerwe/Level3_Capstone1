create database if not exists PoisePMS;
use PoisePMS;
drop table if exists project;
create table project (
projectID int,
projectName varchar(50),
buildingType varchar(50),
projectAddress varchar(250),
ERF varchar(20),
projectFee double,
paidToDate double,
projectDeadline int(4),
projectDateCompleted int(4),
architectID int,
contractorID int,
customerID int,
projectStatus varchar (15),
primary key (projectID));
insert into project values (1001, 'Westlake Church', 'Church', '22 Marlboro Drive', '3365', 15000, 0, 2021, 0, 1, 2, 3, 'Active');
insert into project values (1002, 'Cape Flats', 'Apartments', '23 Green Drive', '3016', 20000, 0, 2022, 0, 1, 2, 4, 'Active');
insert into project values (1003, 'Mauizenberg Store', 'Retail Store', '3 Streand Street', '222', 5000, 3000, 2023, 2021, 5, 2, 4, 'Finalised');
insert into project values (1004, 'Market Box', 'Grocery Store', '22 Green Drive', '3015', 20000, 10000, 2022, 2020, 1, 2, 3, 'Finalised');

Select * FROM project;

drop table if exists personel;
create table personel (
personelID int,
personelType varchar(15),
firstName varchar(50),
surname varchar(50),
email varchar(50),
personelAddress varchar(250),
personelTelephone varchar(12),
primary key (personelID));
insert into personel values (1, 'Architect', 'Martin', 'Sheen', 'sheen@gmail.com', '22 Pritchard Street', '0215589874');
insert into personel values (2, 'Contractor', 'Brian', 'Singer', 'brian@gmail.com','21 Jump Street', '0215589874');
insert into personel values (3, 'Customer', 'Sadie', 'Williams', 'Sadie@gmail.com','14 Mulberry Lane', '0215589874');
insert into personel values (4, 'Customer', 'Olga', 'Spec', 'Olga@gmail.com','4 Bakersfield Park', '0215589874');
insert into personel values (5, 'Architect', 'Mark', 'Wahlberg','Mark@gmail.com','14 Long Street', '0215589874');

Select * FROM personel;

drop table if exists invoice;
create table invoice (
invoiceID int,
customerID int,
feeDue double,
primary key (invoiceID));
insert into invoice values (1, 4, 2000);
insert into invoice values (2, 3, 10000);

Select * FROM invoice;