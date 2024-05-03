/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.view.entry.data;


/**
 *
 * @author k-iderr
 */
public class FolderPermissionDto {
    private Long id;
    private EntryUserDto user;
    private String permissionType;
    private Long folderId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntryUserDto getUser() {
        return user;
    }

    public void setUser(EntryUserDto user) {
        this.user = user;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }
    
    
}
