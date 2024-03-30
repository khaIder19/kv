package com.kv.entry.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author k-iderr
 */
@Entity
@Table(name = "entry_folder")
@SequenceGenerator(name = "seq_gen_id_entry_folder",sequenceName = "seq_gen_id_entry_folder",initialValue = 1,allocationSize = 1)
public class EntryFolder extends BaseEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq_gen_id_entry_folder")
    private Long id;
    
    @Column(name = "name",
            length = 64,
            nullable = false,
            unique = true)
    private String name;
    
    @Column(name = "owner_id",
            nullable = false,
            updatable = false)
    private UUID ownerId;
    
    @ManyToMany
    @JoinTable(name = "entry_folder_permissions",
            joinColumns = {@JoinColumn(name = "entry_folder_id")},
            inverseJoinColumns = {@JoinColumn(name = "folder_permission_id")})
    private List<FolderPermission> permissions;
    
    protected EntryFolder(){}
    
    public EntryFolder(String name,UUID ownerId){
        this.name = name;
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public List<FolderPermission> getPermissions() {
        return permissions;
    }

    public void setName(String name) {
        this.name = name;
    }
}
