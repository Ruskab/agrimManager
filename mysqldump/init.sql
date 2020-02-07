CREATE DATABASE IF NOT EXISTS agrim_manager;

USE agrim_manager;

create table client
(
    id       int auto_increment
        primary key,
    fullName varchar(200) null,
    hours    int          null
)
    engine = MyISAM;

create table intervention
(
    id               int auto_increment
        primary key,
    endTime          datetime     null,
    interventionType int          null,
    startTime        datetime     null,
    title            varchar(255) null,
    repairingPack_id int          null,
    vehicle_id       int          null
)
    engine = MyISAM;

create index FKnnox7lof4f51gwtuklty1v02h
    on intervention (vehicle_id);

create index FKt39d55pulffw05q2f3kj2jn6w
    on intervention (repairingPack_id);

create table mechanic
(
    id       int auto_increment
        primary key,
    name     varchar(255) null,
    password varchar(255) null
)
    engine = MyISAM;

create table mechanic_intervention
(
    Mechanic_id         int not null,
    interventionList_id int not null,
    constraint UK_ssprrhxx6ic80ukmcjufwpub8
        unique (interventionList_id)
)
    engine = MyISAM;

create index FKmdnji9bgd1ybhwallgbnscia3
    on mechanic_intervention (Mechanic_id);

create table mechanic_roles
(
    ID    int          not null,
    roles varchar(255) null
)
    engine = MyISAM;

create index FKitedxqjr9pjdmdp77ho4kgkjh
    on mechanic_roles (ID);

create table repairing_pack
(
    id            int auto_increment
        primary key,
    invoicedDate  date null,
    invoicedHours int  not null
)
    engine = MyISAM;

create table vehicle
(
    id                 int auto_increment
        primary key,
    airFilterReference varchar(255) null,
    bodyOnFrame        varchar(255) null,
    brand              varchar(255) null,
    fuelFilter         varchar(255) null,
    itvDate            date         null,
    kms                varchar(255) null,
    lastRevisionDate   date         null,
    motorOil           varchar(255) null,
    nextItvDate        date         null,
    oilFilterReference varchar(255) null,
    registrationPlate  varchar(255) null,
    client_id          int          null
)
    engine = MyISAM;

create index FKn1vrl9mv7poohmt9xys1ot5p3
    on vehicle (client_id);