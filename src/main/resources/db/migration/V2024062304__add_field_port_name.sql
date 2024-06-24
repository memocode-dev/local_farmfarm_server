ALTER TABLE local_house_section_sensors ADD COLUMN port_name TEXT;

-- 기존 데이터에 대해 기본값 설정
UPDATE local_house_section_sensors SET port_name = 'default_port' WHERE port_name IS NULL;

ALTER TABLE local_house_section_sensors ALTER COLUMN port_name SET NOT NULL;