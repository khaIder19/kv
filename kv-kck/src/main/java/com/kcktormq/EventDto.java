/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kcktormq;

import java.util.Map;
import org.keycloak.events.EventType;

/**
 *
 * @author k-iderr
 */
public class EventDto {
    private Long time;
    private String realm;
    private String clientId;
    private EventType eventType;
    private String userId;
    private Map<String,String> details;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

    public Map<String, String> getDetails() {
        return details;
    }
    
}
