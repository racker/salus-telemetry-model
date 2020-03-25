alter table event_engine_task
    modify task_parameters
    longtext;

alter table monitor_translation_operators
    modify translator_spec
    longtext;

alter table resources
    modify metadata
    longtext;

alter table tenant_metadata
    modify metadata
    longtext;
