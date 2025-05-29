create table address
(
    id bigserial not null primary key,
    street varchar(50)
);

create table client
(
    id   bigserial not null primary key,
    name varchar(50),
    address_id bigint references address(id)
);

create table phone
(
    id bigserial not null primary key,
    client_id bigint references client(id),
    number varchar(50)
);
