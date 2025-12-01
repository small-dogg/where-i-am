CREATE TABLE outbox_event
(
    seq bigserial primary key,
    domain varchar(30) not null,
    topic varchar(50) not null,
    key varchar(256) not null,
    payload text,
    status varchar(30) not null,
    retry_count integer not null,
    created_at timestamp with time zone not null default NOW(),
    last_attempt_at timestamp with time zone
);

CREATE INDEX outbox_event_status_idx ON outbox_event (status);