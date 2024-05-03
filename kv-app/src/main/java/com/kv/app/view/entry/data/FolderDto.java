/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.view.entry.data;

import java.util.List;

/**
 *
 * @author k-iderr
 */
public class FolderDto {
    private Long id;
    private String name;
    private EntryUserDto owner;
    private List<FolderPermissionDto> permissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EntryUserDto getOwner() {
        return owner;
    }

    public void setOwner(EntryUserDto owner) {
        this.owner = owner;
    }

    public List<FolderPermissionDto> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<FolderPermissionDto> permissions) {
        this.permissions = permissions;
    }
    
}
