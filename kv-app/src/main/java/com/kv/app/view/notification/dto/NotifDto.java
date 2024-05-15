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
public class NotifDto {
    
    public static enum NotifStatus{
    CREATED,CONFIRMED,REJECTED,IDLE;
    }
    
    public static enum NotifType{
        ENTRY_CREATED,ENTRY_UPDATED,ENTRY_DELETED,INVITE;
    }    
    
    private Long id;
    private UUID userId;
    private NotifStatus notifStatus;
    private NotifType notifType;
    private String displayDescription;
 
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public NotifStatus getNotifStatus() {
        return notifStatus;
    }

    public void setNotifStatus(NotifStatus notifStatus) {
        this.notifStatus = notifStatus;
    }

    public NotifType getNotifType() {
        return notifType;
    }

    public void setNotifType(NotifType notifType) {
        this.notifType = notifType;
    }

    public String getDisplayDescription() {
        return displayDescription;
    }

    public void setDisplayDescription(String displayDescription) {
        this.displayDescription = displayDescription;
    }
    
    
    
}
