/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.view.notification.dto;

/**
 *
 * @author k-iderr
 */
public class FolderInviteDto {
    private Long folderId;
    private String folderName;
    private String toUser;
    private String permissionType;

    public FolderInviteDto(){
        
    }
    
    public FolderInviteDto(Long folderId, String folderName, String toUser, String permissionType) {
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

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }    
}
