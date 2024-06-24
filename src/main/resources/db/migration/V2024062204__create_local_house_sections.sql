CREATE TABLE local_house_sections
(
    id                    UUID PRIMARY KEY,
    section_number        INT                      NOT NULL,
    version               BIGINT                   NOT NULL,
    local_house_id        UUID                     NOT NULL,
    house_section_id      UUID                     NOT NULL,
    house_section_version BIGINT                   NOT NULL,
    created_at            TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at            TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted               BOOLEAN                  NOT NULL,
    deleted_at            TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_local_house_sections__local_houses FOREIGN KEY (local_house_id) REFERENCES local_houses (id)
);