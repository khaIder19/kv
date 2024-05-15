/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.notif.rest;

import com.kv.notif.asp.event.DaoEvent;
import com.kv.notif.asp.event.DaoEventParam;
import com.kv.notif.dao.NotifUserDao;
import com.kv.notif.dao.NotifDao;
import com.kv.notif.dto.FolderInviteDto;
import com.kv.notif.model.NotifUser;
import com.kv.notif.model.Notif;
import com.kv.notif.model.NotifStatus;
import com.kv.notif.model.NotifType;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.UUID;
/**
 *
 * @author k-iderr
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class NotifRes{
       
    @Inject
    NotifDao dao;
    
    @Inject
    NotifUserDao userDao;

    @Inject
    @Named("subjUUID")
    private UUID subj;
    
    @Inject
    @Named("subjName")
    private String subjName;    
 
    @Path("/notification")
    @GET
    public Response getAllByUser(@QueryParam("notifType")NotifType notifType,@QueryParam("page")Integer index,
            @QueryParam("size")Integer size){
        List<Notif> resultList = List.of();
        Page page = index != null && size != null ? Page.of(index, size) : null;
        if(notifType != null){
            resultList = dao.findByUserIdAndNotifType(subj, notifType,page);
        }else{
            resultList = dao.findByUserId(subj,page);
        }
        Log.debugf("getAllByUser found list with size : %s", resultList.size());
        return Response.ok(resultList).build();
    }
    
    
    @GET
    @Path("/notification/{id}")
    public Response getById(@PathParam("id")Long id){           
        Notif notif = dao.findByIdAndUserId(id,subj);
        if(notif == null) throw new NotFoundException();
        return Response.ok(notif).build();
    }
    
    
    @Transactional
    @Path("/notification/{id}")
    @DELETE  
    public Response delete(@PathParam("id")Long id){
        Notif ef = dao.findByIdAndUserId(id,subj);
        if(ef == null) throw new NotFoundException();
        dao.deleteById(ef.getId());
        return Response.ok().build();
    }
    
    @Transactional
    @Path("/notification/{id}/confirm_notification")
    @PUT
    @DaoEvent
    @DaoEventParam(method = "UPDATE",type = Notif.class)
    public Response confirmNotification(@PathParam("id")Long id){
        Notif ef = dao.findByIdAndUserId(id,subj);
        if(ef == null) throw new NotFoundException();        
        if(ef.getNotifStatus() == NotifStatus.CREATED)ef.setNotifStatus(NotifStatus.CONFIRMED);       
        return Response.ok(ef).build();
    }
    
    @Transactional
    @Path("/notif_user/config")
    @PUT
    public Response configUserNotification(NotifUser nUsrDto){
        NotifUser nu = userDao.findById(subj);
        nu.setEnableFoldersNotifs(nUsrDto.getEnableFoldersNotifs());             
        return Response.ok(nu).build();
    }

    @Path("/notif_user")
    @GET
    public Response getNotifUser(){
        NotifUser nu = userDao.findById(subj);            
        return Response.ok(nu).build();
    }    
    
    @Transactional
    @Path("/invite/send_folder_invite")
    @POST
    @DaoEvent
    @DaoEventParam(method = "CREATE",type = Notif.class)    
    public Response sendFolderInvite(@Valid FolderInviteDto inviteDto){
        NotifUser toUser = userDao.find("name",inviteDto.getToUser()).firstResult();   
        
        boolean inviteAlreadyPresent = false;
        if(toUser != null){
            inviteAlreadyPresent = dao.isNotifAlreadySentByFolderId(toUser.getId(),inviteDto.getFolderId());
        }
        
        if(toUser == null || inviteAlreadyPresent)throw new BadRequestException("exc.invite.act.create.to-user-not-found");
        
        Notif notif = new Notif(toUser, NotifType.INVITE,NotifStatus.CREATED);
        notif.getProps().put(Notif.INVITE_FOLDER_ID,inviteDto.getFolderId().toString());
        notif.getProps().put(Notif.INVITE_FOLDER_NAME,inviteDto.getFolderName());
        notif.getProps().put(Notif.INVITE_FOLDER_FROM_USER,subj.toString());
        notif.getProps().put(Notif.INVITE_FOLDER_FROM_USER_NAME,subjName);        
        notif.getProps().put(Notif.INVITE_FOLDER_TO_USER,toUser.getId().toString());
        notif.getProps().put(Notif.INVITE_FOLDER_PERMISSION_TYPE,inviteDto.getPermissionType().name());        
        dao.createAndDeleteLastInactive(notif);
        
        return Response.created(URI.create("/api/notification/"+notif.getId())).entity(notif).build();
    }
    
}
