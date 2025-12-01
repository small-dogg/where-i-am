CREATE TABLE user_location
(
    seq       bigserial primary key,
    user_seq  bigint        not null,
    latitude  numeric(9, 6) not null,
    longitude numeric(9, 6) not null,
    created_at timestamp with time zone not null
);

CREATE INDEX user_location_user_seq_created_at ON user_location (user_seq, created_at);
CREATE INDEX user_location_created_at ON user_location (created_at);
