/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.kv.entry.dto.DtoViews;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/**
 *
 * @author k-iderr
 */
@Entity
@Table(name = "entry",
        uniqueConstraints = {
            @UniqueConstraint(name = "uniq_entry_key",columnNames = { "key", "folder_id" }) 
        }
)
@SequenceGenerator(name = "seq_gen_id_entry",sequenceName = "seq_gen_id_entry",initialValue = 1,allocationSize = 1)
public class Entry extends BaseEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq_gen_id_entry")
    private Long id;
    
    @Column(name = "key")
    @NotBlank(message = "exc.constr.Entry.key.not-null")
    @Size(max = 32,message = "exc.constr.Entry.key.size")
    private String key;
    
    @Column(name = "value")
    private String value;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    @JsonView(DtoViews.Event.class)
    private Folder folder;
    
    @Column(name = "folder_id",insertable = false,updatable = false)
    private Long folderId;
    
    @Column(name = "modified_by")
    @NotNull
    private UUID modifiedBy;    
    
    
    protected Entry(){}
    
    public Entry(Folder folder,String key,String value){
        this.folder = folder;
        this.key = key;
        this.value = value;
        this.folderId = folder.getId();
    }
    
    private Entry(Long id,Folder folder,String key,String value,UUID modifiedBy){
        this.id = id;
        this.folderId = folder.getId();
        this.folder = new Folder(folder.getName(),
                new EntryUser(folder.getOwner().getId(),folder.getOwner().getName()));
        this.key = key;
        this.value = value;
        this.modifiedBy = modifiedBy;
    }
    
    public Entry asPojo(){
        return new Entry(getId().longValue(), getFolder(),
                getKey().toString(), getValue().toString(),UUID.fromString(modifiedBy.toString()));
    }

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Folder getFolder() {
        return folder;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setModifiedBy(UUID modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public UUID getModifiedBy() {
        return modifiedBy;
    }

    @Override
    public String toString() {
        return "Entry{" + "id=" + id + ", key=" + key + ", value=" + value + ", folder=" + folder + ", folderId=" + folderId + '}';
    }
}
