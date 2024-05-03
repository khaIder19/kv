/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.asp.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kv.app.common.UserSessionParamDto;
import com.kv.app.common.UserSessionParamRegistry;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.SystemException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

/**
 *
 * @author k-iderr
 */
@ApplicationScoped
public class UserEventEvService {
     
    @Inject
    ObjectMapper objMapper;
    
    @Inject
    UserSessionParamRegistry userParamRegistry;
    
    @ConfigProperty(name = "kv.realm-id")
    String realmId;
       
    
    @Incoming("kck-events-kv-app")
    @Blocking
    public CompletionStage<Void> onUserEvent(Message msg) throws SystemException{
        try{
        Log.info("kv app event received");
        byte[] body = (byte[]) msg.getPayload();
        EntryUserRegEventDto dto = objMapper.readValue(body, EntryUserRegEventDto.class);
        UUID userUUID = dto.getUserUUID();
        
        if(userUUID != null && dto.getRealm().equals(realmId)){               
            switch (dto.getEventType()) {
                case "UPDATE_PROFILE":
                    UserSessionParamDto userParams = userParamRegistry.getByUserId(userUUID);
                    if(userParams != null){
                    if(dto.getUserName() != null){
                        userParams.setUsername(dto.getUserName());
                    }
                    if(dto.getLocale() != null){
                        userParams.setLocale(dto.getLocale());
                    }
                    }
                    break;                   
            }
        }
        }catch(Exception e){
            Log.errorf("Erro during user event registration",e);
            return msg.nack(e);
        }
        return msg.ack();
    }
    
    
    
    private static class EntryUserRegEventDto{
        private long time;
        private String userId;
        private String realm;
        private String eventType;
        private Map<String,String> details;

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getRealm() {
            return realm;
        }

        public void setRealm(String realm) {
            this.realm = realm;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public Map<String, String> getDetails() {
            return details;
        }

        public void setDetails(Map<String, String> details) {
            this.details = details;
        }
        
        public UUID getUserUUID(){
            UUID uuid = null;
            if(userId != null){
                uuid = UUID.fromString(userId);
            }
            return uuid;
        }
        
        public String getUserName(){
            String userName = null;
            if(details != null){
                if(eventType.equals("REGISTER")){
                    userName = details.get("username");
                }else if(eventType.equals("UPDATE_PROFILE")){
                    userName = details.get("updated_username");                    
                }
            }
            return userName;
        }
        
        public String getLocale(){
            String locale = null;
            if(details != null){
                if(eventType.equals("UPDATE_PROFILE")){
                    locale = details.get("updated_locale");
                }
            }
            return locale;
        }
    }    
}
