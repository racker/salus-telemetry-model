create table agent_history
(
    id                    varchar(255)               not null
        primary key,
    connected_timestamp     datetime                 not null,
    disconnected_timestamp  datetime                 null,
    tenant_id             varchar(255)               not null,
    resource_id           varchar(255)               not null,
    envoy_id              varchar(255)               not null,
    remote_ip             varchar(255)               not null,
    zone_id               varchar(255)               null
)
    engine = InnoDB;

create index byTenantOrEnvoyId
    on agent_history (tenant_id, envoy_id);