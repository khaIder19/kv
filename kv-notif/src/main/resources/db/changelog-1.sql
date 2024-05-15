--liquibase formatted sql

--changeset kiderr:notif_user_ddl
CREATE TABLE notif_user (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    enable_folders_notifs BOOLEAN NOT NULL,
    "name" VARCHAR(16) NOT NULL
);
ALTER TABLE notif_user ADD CONSTRAINT uniq_notif_user_name UNIQUE ("name");

--changeset kiderr:notif_ddl
CREATE SEQUENCE IF NOT EXISTS seq_gen_id_notif;
CREATE TABLE notif (
    id BIGINT PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    user_id UUID NOT NULL,
    notif_type SMALLINT NOT NULL,
    notif_status SMALLINT NOT NULL
);
ALTER TABLE notif ADD CONSTRAINT fk_notif_user_id 
FOREIGN KEY (user_id) REFERENCES notif_user (id) ON DELETE CASCADE;

--changeset kiderr:notif_props_ddl
CREATE TABLE notif_props (
    id SERIAL PRIMARY KEY,
    notif_id BIGINT NOT NULL,
    prop_name VARCHAR(64) NOT NULL,
    prop_value VARCHAR(64) NOT NULL
);
ALTER TABLE notif_props ADD CONSTRAINT fk_notif_props_notif_id 
FOREIGN KEY (notif_id) REFERENCES notif (id) ON DELETE CASCADE;

