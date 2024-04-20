package com.kv.entry.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 *
 * @author k-iderr
 */
@Entity
@Table(name = "folder",
        uniqueConstraints = {
            @UniqueConstraint(name = "uniq_folder_name",columnNames = { "name", "owner_id" }) 
        }
)
@SequenceGenerator(name = "seq_gen_id_folder",sequenceName = "seq_gen_id_folder",initialValue = 1,allocationSize = 1)
public class Folder extends BaseEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq_gen_id_folder")
    private Long id;
    
    @Column(name = "name")
    @NotBlank(message = "exc.constr.Folder.name.not-null")
    @Size(max = 32,message = "exc.constr.Folder.name.size")
    private String name;
    
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "owner_id",updatable = false)
    private EntryUser owner;
    
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "folder")
    @JsonManagedReference
    private List<FolderPermission> permissions;
    
    protected Folder(){}
    
    public Folder(String name,EntryUser owner){
        this.name = name;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public EntryUser getOwner() {
        return owner;
    }

    public List<FolderPermission> getPermissions() {
        return permissions;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Folder{" + "id=" + id + ", name=" + name + ", owner=" + owner + '}';
    }
}
