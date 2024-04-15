/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.view.entry.data;

import java.util.UUID;

/**
 *
 * @author k-iderr
 */
public class EntryUserDto {
    private UUID id;
    private String name;
    
    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    
}
