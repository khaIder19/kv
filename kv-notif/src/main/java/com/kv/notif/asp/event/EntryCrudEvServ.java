/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.notif.asp.event;

import com.kv.notif.dao.NotifDao;
import com.kv.notif.dao.NotifUserDao;
import com.kv.notif.model.Notif;
import com.kv.notif.model.NotifStatus;
import com.kv.notif.model.NotifType;
import com.kv.notif.model.NotifUser;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.SystemException;
import jakarta.transaction.Transactional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

/**
 *
 * @author k-iderr
 */
@ApplicationScoped
public class EntryCrudEvServ {
    
    @Inject
    NotifUserDao dao;
    
    @Inject
    NotifDao notifDao;    
       
    
    @Incoming("kv-notif-entry-crud-events")
    @Blocking
    @Broadcast
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public CompletionStage<Void> onEntryEvent(Message msg) throws SystemException{
        try{
        Log.debug("Message received :"+msg.getPayload().toString());
        JsonObject eventJson = (JsonObject) msg.getPayload();
        
        
        if(eventJson.getString("eventType").equals("CRUD_EVENT") && 
                eventJson.getString("payloadType").equals("Entry")){               
            
            
            JsonObject payload = eventJson.getJsonObject("payload");
            JsonObject entryFolder = payload.getJsonObject("folder");
            JsonObject ownerFolder = entryFolder.getJsonObject("owner");
            
            String entryKey = payload.getString("key");
            String folderName = entryFolder.getString("name");
            UUID folderOwner = UUID.fromString(ownerFolder.getString("id"));
            UUID modifiedBy = UUID.fromString(payload.getString("modifiedBy"));
            
            if(modifiedBy.equals(folderOwner))return msg.ack();
            
            
            NotifUser notifUser = dao.findById(folderOwner);
            NotifType notifType = null;
            
            if(notifUser.getEnableFoldersNotifs()){            
            switch (eventJson.getString("crudType")) {
                case "CREATE":
                    notifType = NotifType.ENTRY_CREATED;
                    break;
                case "UPDATE":
                    notifType = NotifType.ENTRY_UPDATED;                    
                    break;
                case "DELETE":
                    notifType = NotifType.ENTRY_DELETED;                    
                    break;                    
            }
            
            if(notifType != null){
                Notif notif = new Notif(notifUser, notifType,NotifStatus.IDLE);
                notif.getProps().put(Notif.ENTRY_KEY, entryKey);
                notif.getProps().put(Notif.ENTRY_FOLDER_NAME, folderName);            
            
                notifDao.createAndDeleteLastInactive(notif);
            }else{
                Log.warn("Notif type cant be handler");                
            }
            
            }
        }
        }catch(Exception e){
            Log.errorf("Error during entry dto event handling",e);            
            return msg.nack(e);
        }
        return msg.ack();
    }

    
    
}
