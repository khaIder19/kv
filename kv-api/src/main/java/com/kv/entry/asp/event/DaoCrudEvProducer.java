/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.asp.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kv.entry.dto.BaseDaoEventDto;
import com.kv.entry.dto.DtoViews;
import io.quarkus.logging.Log;
import io.vertx.core.json.JsonObject;
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
  
    @Channel("kv-crud-events")Emitter<JsonObject> emitter;
   
    private ObjectMapper eventObjMapper;
    
    public DaoCrudEvProducer(){
        eventObjMapper = new ObjectMapper();
    }
    
    public boolean publishDaoEvent(String method,String type,Object entity){
        try {
            BaseDaoEventDto dto = buildBaseDaoEventDto(method, type, entity);
            String dtoAsJson = eventObjMapper.writerWithView(DtoViews.Event.class)
                    .writeValueAsString(dto);
            publishFinalMessage(dtoAsJson);
            Log.debugf("Dao event message sent, with etity : %s , method : %s , type : %s",
                    entity,method,type);
            return true;
        } catch (JsonProcessingException ex) {
            Log.errorf("Fail to serialize dao event dto for entity : %s , method : %s , type : %s",
                                entity.toString(),method,type);
        }
        return false;
    }
    

    void publishFinalMessage(String eventAsJson){
        emitter.send(new JsonObject(eventAsJson));
    }
    
    BaseDaoEventDto buildBaseDaoEventDto(String method,String type,Object entity){       
        BaseDaoEventDto dto = new BaseDaoEventDto();        
        dto.setTime(Instant.now().getEpochSecond());
        dto.setEventType("CRUD_EVENT");
        dto.setPayloadType(type);
        dto.setCrudType(method);
        dto.setPayload(entity);
        return dto;
    }
}
