package com.kv.entry.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

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
    
    @OneToOne
    @JoinColumn(name = "user_id",updatable = false)
    private EntryUser user;
    
    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "permission_type",nullable = false)
    private FolderPermissionType permissionType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    @JsonBackReference
    private Folder folder;
    
    
    protected FolderPermission(){}
    
    public FolderPermission(EntryUser user,Folder folder,FolderPermissionType permissionType){
        this.user = user;
        this.permissionType = permissionType;
        this.folder = folder;
    }

    public Long getId() {
        return id;
    }

    public EntryUser getUser() {
        return user;
    }

    public FolderPermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(FolderPermissionType permissionType) {
        this.permissionType = permissionType;
    }

    public Folder getFolder() {
        return folder;
    }
}
