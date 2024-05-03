/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.common;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author k-iderr
 */
@ApplicationScoped
public class UserSessionParamRegistry {
    
    private Map<UUID,UserSessionParamDto> paramMap;
    
    public UserSessionParamRegistry(){
        this.paramMap = new HashMap<>();
    }
    
    public UserSessionParamDto getByUserId(UUID userUuid){
        return paramMap.get(userUuid);
    }
    
    public void addUserSessionParam(UserSessionParamDto paramsDto){
        paramMap.put(paramsDto.getUserUUID(), paramsDto);
    }
    
    public void removeSessionParamForUser(UUID userUuid){
        paramMap.remove(userUuid);
    }
}
