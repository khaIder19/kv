--liquibase formatted sql

--changeset kiderr:entry_user_ddl
CREATE TABLE entry_user (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    "name" VARCHAR(16) NOT NULL
);
ALTER TABLE entry_user ADD CONSTRAINT uniq_entry_user_name UNIQUE ("name");

--changeset kiderr:folder_ddl
CREATE SEQUENCE IF NOT EXISTS seq_gen_id_folder;
CREATE TABLE folder (
    id BIGINT PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    "name" VARCHAR(32) NOT NULL,
    owner_id UUID NOT NULL
);
ALTER TABLE folder ADD CONSTRAINT fk_folder_owner_id 
FOREIGN KEY (owner_id) REFERENCES entry_user (id) ON DELETE CASCADE;

ALTER TABLE folder ADD CONSTRAINT uniq_folder_name UNIQUE ("name", owner_id);

--changeset kiderr:folder_permission_ddl
CREATE SEQUENCE IF NOT EXISTS seq_gen_id_folder_permission;
CREATE TABLE folder_permission (
    id BIGINT PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    user_id UUID NOT NULL,
    folder_id BIGINT,    
    permission_type SMALLINT NOT NULL
);
ALTER TABLE folder_permission ADD CONSTRAINT fk_folder_permission_folder_id 
FOREIGN KEY (folder_id) REFERENCES folder (id) ON DELETE CASCADE;

ALTER TABLE folder_permission ADD CONSTRAINT fk_folder_permission_user_id 
FOREIGN KEY (user_id) REFERENCES entry_user (id) ON DELETE CASCADE;

--changeset kiderr:entry_ddl
CREATE SEQUENCE IF NOT EXISTS seq_gen_id_entry;
CREATE TABLE entry (
    id BIGINT PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    modified_by UUID NOT NULL,
    "key" VARCHAR(32) NOT NULL,
    "value" TEXT,
    folder_id BIGINT
);

ALTER TABLE entry ADD CONSTRAINT fk_entry_folder_id 
FOREIGN KEY (folder_id) REFERENCES folder (id) ON DELETE CASCADE;

ALTER TABLE entry ADD CONSTRAINT uniq_entry_key UNIQUE ("key", folder_id);
