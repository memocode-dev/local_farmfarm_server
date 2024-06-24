CREATE TABLE local_houses
(
    id            UUID PRIMARY KEY,
    name          TEXT                     NOT NULL,
    version       INT                      NOT NULL,
    house_id      UUID                     NOT NULL,
    house_version INT                      NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at    TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted       BOOLEAN                  NOT NULL,
    deleted_at    TIMESTAMP WITH TIME ZONE NOT NULL
);