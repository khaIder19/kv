/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.view.notification.dto;

import java.util.UUID;

/**
 *
 * @author k-iderr
 */
public class NotifUserDto {
    private UUID id;
    private String name;
    private Boolean enableFoldersNotifs;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnableFoldersNotifs() {
        return enableFoldersNotifs;
    }

    public void setEnableFoldersNotifs(Boolean enableFoldersNotifs) {
        this.enableFoldersNotifs = enableFoldersNotifs;
    }
    
    
}
