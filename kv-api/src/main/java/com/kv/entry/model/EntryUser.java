/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/**
 *
 * @author k-iderr
 */
@Entity
@Table(name = "entry_user")
public class EntryUser extends BaseEntity{
    
    @Id
    @Column(name="id")
    private UUID id;
    
    @Column(name = "name")
    @NotNull
    private String name;
    
    protected EntryUser(){}
    
    public EntryUser(UUID id,String name){
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
}
