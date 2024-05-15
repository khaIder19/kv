/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.notif.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

/**
 *
 * @author k-iderr
 */
@Entity
@Table(name = "notif_user")
public class NotifUser extends BaseEntity{
    
    @Id
    @Column(name="id")
    private UUID id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "enable_folders_notifs")
    private Boolean enableFoldersNotifs;
    
    protected NotifUser(){}
    
    public NotifUser(UUID id,String name,boolean enableFoldersNotifs){
        this.id = id;
        this.name = name;
        this.enableFoldersNotifs = enableFoldersNotifs;
    }

    public UUID getId() {
        return id;
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
