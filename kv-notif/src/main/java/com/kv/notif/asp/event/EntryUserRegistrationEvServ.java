/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.notif.asp.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kv.notif.dao.NotifUserDao;
import com.kv.notif.model.NotifUser;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.SystemException;
import jakarta.transaction.Transactional;
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
public class EntryUserRegistrationEvServ {
    
    @Inject
    NotifUserDao dao;
    
    @Inject
    ObjectMapper objMapper;
    
    @ConfigProperty(name = "kv.realm-id")
    String realmId;
       
    
    @Incoming("kck-events-kv-notif")
    @Blocking
    @Broadcast
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public CompletionStage<Void> onUserEvent(Message msg) throws SystemException{
        try{
        byte[] body = (byte[]) msg.getPayload();
        EntryUserRegEventDto dto = objMapper.readValue(body, EntryUserRegEventDto.class);
        UUID userUUID = dto.getUserUUID();
        
        if(userUUID != null && dto.getRealm().equals(realmId)){               
            NotifUser entryUser = dao.findById(userUUID);
            switch (dto.getEventType()) {
                case "REGISTER":
                    entryUser = new NotifUser(userUUID, dto.getUserName(),true);
                    dao.persist(entryUser);
                    Log.infof("Attempt to create NitifUser with id = %s AND name = %s",
                            userUUID.toString(),dto.getUserName());
                    break;
                case "UPDATE_PROFILE":
                    if(dto.getUserName() != null){
                    entryUser.setName(dto.getUserName());
                    Log.infof("Attempt to update NitifUser with id = %s AND name = %s",
                            userUUID.toString(),dto.getUserName());
                    }                    
                    break;
                case "DELETE_ACCOUNT":
                    dao.deleteById(userUUID);
                    Log.infof("Attempt to delete NitifUser with id = %s",
                            userUUID.toString());               
                    break;                    
            }
        }
        }catch(Exception e){
            Log.errorf("Erro during user even registration",e);
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
    }
}
