create table jobs
(
    id                varchar(45) not null primary key,
    name              varchar(45) not null,
    tenant_id         varchar(45) null,
    status            varchar(45) not null,
    description       text        null,
    created_timestamp datetime    null,
    updated_timestamp datetime    null
);