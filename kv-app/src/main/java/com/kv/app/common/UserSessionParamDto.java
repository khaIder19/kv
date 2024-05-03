/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.common;

import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author k-iderr
 */
public class UserSessionParamDto {
    private UUID userUUID;
    private String username;
    private String Locale;

    public UserSessionParamDto(UUID userUUID, String username, String Locale) {
        this.userUUID = userUUID;
        this.username = username;
        this.Locale = Locale;
    }

    public UUID getUserUUID() {
        return userUUID;
    }
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocale() {
        return Locale;
    }

    public void setLocale(String Locale) {
        this.Locale = Locale;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.userUUID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserSessionParamDto other = (UserSessionParamDto) obj;
        return Objects.equals(this.userUUID, other.userUUID);
    }
    
}
