/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.notif.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author k-iderr
 */
public class FolderInviteDto {

    public enum FolderPermissionType{
        READ,WRITE;
    }

    @NotNull
    private Long folderId;
    @NotBlank
    private String folderName;
    @NotNull
    private String toUser;
    @NotNull
    private FolderPermissionType permissionType;

    public FolderInviteDto(){
        
    }
    
    public FolderInviteDto(Long folderId, String folderName, String toUser, FolderPermissionType permissionType) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.toUser = toUser;
        this.permissionType = permissionType;
    }

    
    
    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public FolderPermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(FolderPermissionType permissionType) {
        this.permissionType = permissionType;
    }
    
    
}
