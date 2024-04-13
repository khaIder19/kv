/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.model;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
    @NotNull
    @Size(max = 32)
    private String key;
    
    @Column(name = "value")
    private String value;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;
    
    protected Entry(){}
    
    public Entry(Folder folder,String key,String value){
        this.folder = folder;
        this.key = key;
        this.value = value;
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
}
