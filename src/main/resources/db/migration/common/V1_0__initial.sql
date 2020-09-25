create table agent_releases
(
    id                varchar(255) not null
        primary key,
    created_timestamp datetime     null,
    exe               varchar(255) not null,
    type              varchar(255) not null,
    updated_timestamp datetime     null,
    url               varchar(255) not null,
    version           varchar(255) not null
)
    engine = InnoDB;

create table agent_install_label_selectors
(
    agent_install_id   varchar(255) not null,
    label_selector     varchar(255) null,
    label_selector_key varchar(255) not null,
    primary key (agent_install_id, label_selector_key)
)
    engine = InnoDB;

create table agent_installs
(
    id                    varchar(255)               not null
        primary key,
    created_timestamp     datetime                   null,
    tenant_id             varchar(255)               not null,
    updated_timestamp     datetime                   null,
    agent_release_id      varchar(255)               not null,
    label_selector_method varchar(255) default 'AND' not null,
    FOREIGN KEY (agent_release_id) REFERENCES agent_releases(id)
)
    engine = InnoDB;

create index by_tenant
    on agent_installs (tenant_id);

create table agent_release_labels
(
    agent_release_id varchar(255) not null,
    labels           varchar(255) null,
    labels_key       varchar(255) not null,
    primary key (agent_release_id, labels_key)
)
    engine = InnoDB;

create index by_type
    on agent_releases (type);

create table bound_agent_installs
(
    resource_id      varchar(255) not null,
    agent_install_id varchar(255) not null,
    primary key (agent_install_id, resource_id)
)
    engine = InnoDB;

create table bound_monitors
(
    resource_id       varchar(100) not null,
    tenant_id         varchar(255) not null,
    zone_name         varchar(100) not null,
    created_timestamp datetime     null,
    envoy_id          varchar(100) null,
    rendered_content  longtext     null,
    updated_timestamp datetime     null,
    monitor_id        varchar(255) not null,
    primary key (monitor_id, resource_id, tenant_id, zone_name)
)
    engine = InnoDB;

create index by_envoy_id
    on bound_monitors (envoy_id);

create index by_resource
    on bound_monitors (resource_id);

create index by_zone_envoy
    on bound_monitors (zone_name, envoy_id);

create table event_engine_task
(
    id                varchar(255) not null
        primary key,
    created_timestamp datetime     null,
    kapacitor_task_id varchar(255) not null,
    measurement       varchar(255) not null,
    name              varchar(255) not null,
    task_parameters   longtext not null,
    tenant_id         varchar(255) not null,
    updated_timestamp datetime     null
)
    engine = InnoDB;

create index tenant_measures
    on event_engine_task (tenant_id, measurement);


create table metadata_policies
(
    metadata_key      varchar(255) null,
    target_class_name varchar(255) not null,
    metadata_value    varchar(255) null,
    value_type        varchar(255) not null,
    id                varchar(255) not null
        primary key
)
    engine = InnoDB;

create table monitor_label_selectors
(
    monitor_id         varchar(255) not null,
    label_selector     varchar(255) null,
    label_selector_key varchar(255) not null,
    primary key (monitor_id, label_selector_key)
)
    engine = InnoDB;

create table monitor_metadata_fields
(
    monitor_id              varchar(255) not null,
    monitor_metadata_fields varchar(255) null
)
    engine = InnoDB;

create index monitors_by_metadata_field
    on monitor_metadata_fields (monitor_id);

create table monitor_metadata_policies
(
    monitor_type varchar(255) null,
    id           varchar(255) not null
        primary key
)
    engine = InnoDB;

create table monitor_policies
(
    monitor_id varchar(255) not null,
    name       varchar(255) not null,
    id         varchar(255) not null
        primary key
)
    engine = InnoDB;

create table monitor_translation_operators
(
    id              varchar(255) not null
        primary key,
    agent_type      varchar(255) not null,
    agent_versions  varchar(255) null,
    monitor_type    int          null,
    selector_scope  int          null,
    translator_spec longtext not null,
    name            varchar(255) not null,
    constraint UK_ovai2pinwx763vvrmdwobtl4p
        unique (name)
)
    engine = InnoDB;

create index monitor_translation_operators_by_agent_type
    on monitor_translation_operators (agent_type);

create table monitors
(
    id                    varchar(255)  not null
        primary key,
    agent_type            varchar(255)  not null,
    content               varchar(1000) not null,
    created_timestamp     datetime      null,
    monitoring_interval   bigint        not null,
    label_selector_method varchar(255)  not null,
    monitor_name          varchar(255)  null,
    monitor_type          varchar(255)  not null,
    resource_id           varchar(255)  null,
    selector_scope        varchar(255)  null,
    tenant_id             varchar(255)  not null,
    updated_timestamp     datetime      null
)
    engine = InnoDB;

create index monitors_by_tenant_and_resource
    on monitors (tenant_id, resource_id);

create table monitor_zones
(
    monitor_id varchar(255) not null,
    zones      varchar(255) null,
    foreign key (monitor_id) references monitors(id)
)
    engine = InnoDB;

create table plugin_metadata_fields
(
    monitor_id             varchar(255) not null,
    plugin_metadata_fields varchar(255) null
)
    engine = InnoDB;

create index monitors_by_plugin_metadata_field
    on plugin_metadata_fields (monitor_id);

create table policies
(
    id                varchar(255) not null
        primary key,
    created_timestamp datetime     null,
    scope             varchar(255) not null,
    subscope          varchar(255) null,
    updated_timestamp datetime     null
)
    engine = InnoDB;

create table resource_labels
(
    id         bigint       not null,
    labels     varchar(255) null,
    labels_key varchar(255) not null,
    primary key (id, labels_key)
)
    engine = InnoDB;

create index IDXsfyp9clv4p78phb65j12dp8av
    on resource_labels (id, labels_key, labels);

create table resources
(
    id                          bigint       not null
        primary key,
    associated_with_envoy       bit          not null,
    created_timestamp           datetime     null,
    metadata                    longtext         null,
    presence_monitoring_enabled bit          not null,
    resource_id                 varchar(255) not null,
    tenant_id                   varchar(255) not null,
    updated_timestamp           datetime     null,
    constraint by_tenant_resource unique (tenant_id, resource_id)
)
    engine = InnoDB;

create table tenant_metadata
(
    id                varchar(255) not null
        primary key,
    account_type      varchar(255) null,
    created_timestamp datetime     null,
    metadata          longtext         not null,
    tenant_id         varchar(255) not null,
    updated_timestamp datetime     null,
    constraint by_tenant unique (tenant_id)
)
    engine = InnoDB;

create index by_account_type
    on tenant_metadata (account_type);

create index metadata_by_tenant
    on tenant_metadata (tenant_id);

create table zones
(
    id                varchar(255) not null
        primary key,
    created_timestamp datetime     null,
    is_public         bit          not null,
    name              varchar(255) not null,
    poller_timeout    bigint       null,
    provider          varchar(255) null,
    provider_region   varchar(255) null,
    state             varchar(255) null,
    tenant_id         varchar(255) not null,
    updated_timestamp datetime     null,
    constraint by_tenant_name unique (tenant_id, name)
)
    engine = InnoDB;

create table zone_source_ip_addresses
(
    zone_id    varchar(255) not null,
    source_ips varchar(255) null,
    foreign key (zone_id) references zones(id)
)
    engine = InnoDB;
