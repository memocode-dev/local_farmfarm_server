CREATE TABLE house_section_sensors
(
    id                           UUID PRIMARY KEY,
    name_for_admin               TEXT                     NOT NULL,
    name_for_user                TEXT                     NOT NULL,
    sensor_model                 TEXT                     NOT NULL,
    version                      BIGINT                   NOT NULL,
    house_section_sensor_version BIGINT                   NOT NULL,
    house_section_sensor_id      UUID                     NOT NULL,
    local_house_section_id       UUID                     NOT NULL,
    created_at                   TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at                   TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted                      BOOLEAN                  NOT NULL,
    deleted_at                   TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_house_section_sensors__house_sections FOREIGN KEY (local_house_section_id) REFERENCES local_house_sections (id)
);