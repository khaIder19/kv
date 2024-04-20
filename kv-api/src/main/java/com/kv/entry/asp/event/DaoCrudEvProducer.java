/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.asp.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

/**
 *
 * @author k-iderr
 */

@ApplicationScoped
public class DaoCrudEvProducer {    
  
    @Channel("kv-entry-crud-events")Emitter<BaseDaoEventDto> emitter;
   
    
    public boolean publishDaoEvent(String method,String type,Object entity){
        try {
            BaseDaoEventDto dto = buildBaseDaoEventDto(method, type, entity);
            publishFinalMessage(dto);
            Log.debugf("Dao event message sent, with etity : %s , method : %s , type : %s",
                    entity,method,type);
            return true;
        } catch (JsonProcessingException ex) {
            Log.errorf("Fail to serialize dao event dto for entity : %s , method : %s , type : %s",
                                entity.toString(),method,type);
        }
        return false;
    }
    

    void publishFinalMessage(BaseDaoEventDto eventDto){
        emitter.send(eventDto);
    }
    
    BaseDaoEventDto buildBaseDaoEventDto(String method,String type,Object entity) throws JsonProcessingException{       
        BaseDaoEventDto dto = new BaseDaoEventDto();        
        dto.setTime(Instant.now().getEpochSecond());
        dto.setEventType("CRUD_EVENT");
        dto.setPayloadType(type);
        dto.setCrudType(method);
        dto.setPayload(entity);
        return dto;
    }
    
    private static class BaseDaoEventDto{
        private long time;
        private String eventType;
        private String payloadType;
        private String crudType;
        private Object payload;

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public String getPayloadType() {
            return payloadType;
        }

        public void setPayloadType(String payloadType) {
            this.payloadType = payloadType;
        }

        public String getCrudType() {
            return crudType;
        }

        public void setCrudType(String crudType) {
            this.crudType = crudType;
        }

        public Object getPayload() {
            return payload;
        }

        public void setPayload(Object payload) {
            this.payload = payload;
        }
        
        
    }
}
