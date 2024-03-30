/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 *
 * @author k-iderr
 */
@Entity
@Table(name = "entry")
@SequenceGenerator(name = "seq_gen_id_entry",sequenceName = "seq_gen_id_entry",initialValue = 1,allocationSize = 1)
public class Entry extends BaseEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq_gen_id_entry")
    private Long id;
    
    @Column(name = "key",
            nullable = false,
            unique = true,
            length = 64)
    private String key;
    
    @Column(name = "value")
    private String value;
    
    @ManyToOne
    @Column(name = "entry_folder_id")
    private EntryFolder entryFolder;
    
    protected Entry(){}
    
    public Entry(EntryFolder entryFolder,String key,String value){
        this.entryFolder = entryFolder;
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

    public EntryFolder getEntryFolder() {
        return entryFolder;
    }
}
