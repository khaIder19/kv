package com.kv.entry.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.UUID;

/**
 *
 * @author k-iderr
 */
@Entity
@Table(name = "folder_permission")
@SequenceGenerator(name = "seq_gen_id_folder_permission",sequenceName = "seq_gen_id_folder_permission",initialValue = 1,allocationSize = 1)
public class FolderPermission extends BaseEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq_gen_id_folder_permission")
    private Long id;
    
    @Column(name = "user_id",
            nullable = false,
            updatable = false)
    private UUID userId;
    
    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "permission_type",nullable = false)
    private FolderPermissionType permissionType;
    
    protected FolderPermission(){}
    
    public FolderPermission(UUID userId,FolderPermissionType permissionType){
        this.userId = userId;
        this.permissionType = permissionType;
    }

    public Long getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public FolderPermissionType getPermissionType() {
        return permissionType;
    }
}
