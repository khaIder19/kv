/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.asp.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kv.entry.dao.EntryUserDao;
import com.kv.entry.dao.FolderDao;
import com.kv.entry.dao.FolderPermissionDao;
import com.kv.entry.model.EntryUser;
import com.kv.entry.model.Folder;
import com.kv.entry.model.FolderPermission;
import com.kv.entry.model.FolderPermissionType;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.SystemException;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

/**
 *
 * @author k-iderr
 */
@ApplicationScoped
public class FolderInviteEvServ {

    @Inject
    ObjectMapper objMapper;
    
    @Inject
    FolderPermissionDao folderPermissionDao;
    
    @Inject
    EntryUserDao entryUserDao;
    
    @Inject
    FolderDao folderDao;    
    
    @Incoming("kv-entry-notif-crud-events")
    @Blocking
    @Broadcast
    @Transactional(Transactional.TxType.REQUIRES_NEW)    
    public CompletionStage<Void> onEntryEvent(Message msg) throws SystemException{
        try{
        JsonObject eventDto = (JsonObject) msg.getPayload();
        if(eventDto.getString("eventType").equals("CRUD_EVENT") &&
                eventDto.getString("payloadType").equals("Notif")){
        
        if(eventDto.getString("crudType").equals("UPDATE")){
        
                JsonObject jsonObject = eventDto.getJsonObject("payload");
                if(jsonObject.getString("notifType").equals("INVITE") &
                    jsonObject.getString("notifStatus").equals("CONFIRMED")){
                    
                    handleNotifPayload(jsonObject);
                }   
            }             
        }
          
        }catch(Exception e){
            Log.errorf("Erro during folder invite accept handling",e);
            return msg.nack(e);
        }
        return msg.ack();        
    }
    
    
    public void handleNotifPayload(JsonObject jsonObj) throws JsonProcessingException{
        TypeReference<HashMap<String, String>> typeRef 
            = new TypeReference<HashMap<String, String>>() {};
        JsonObject props = jsonObj.getJsonObject("props");
        
        UUID ownerUserId = UUID.fromString(props.getString("invite_folder_from_user"));
        UUID toUserId = UUID.fromString(props.getString("invite_folder_to_user"));
        Long folderId = Long.valueOf(props.getString("invite_folder_id"));
        FolderPermissionType permissionType = FolderPermissionType.valueOf(props.getString("invite_folder_permission_type"));
        
        Folder folder = folderDao.findById(folderId);
        if(folder != null && folder.getOwner().getId().equals(ownerUserId)){
            EntryUser toUser = entryUserDao.findById(toUserId);    
            if(toUser != null){
            
            FolderPermission fp = folderPermissionDao.findByFolderAndUser(folderId, toUserId);
            if(fp == null){
                fp = new FolderPermission(toUser,folder, permissionType);
                folderPermissionDao.persist(fp);
            }else{
                fp.setPermissionType(permissionType);
            }    
             
            }
        }       
    }
}
