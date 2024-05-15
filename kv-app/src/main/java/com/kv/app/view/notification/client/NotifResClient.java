/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.kv.app.view.notification.client;

import com.kv.app.exc.BaseRestExcMapper;
import com.kv.app.view.notification.dto.FolderInviteDto;
import com.kv.app.view.notification.dto.NotifDto;
import com.kv.app.view.notification.dto.NotifUserDto;
import io.quarkus.oidc.token.propagation.AccessTokenRequestFilter;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author k-iderr
 */
@RegisterRestClient
@RegisterProvider(AccessTokenRequestFilter.class)
@RegisterProvider(BaseRestExcMapper.class)
@Path("/api")
public interface NotifResClient {

    @GET
    @Path("/notification")       
    List<NotifDto> getAllNotifsByUser(@QueryParam("page")Integer page,
            @QueryParam("size")Integer size,@HeaderParam("lang")String lang);
    
    @GET
    @Path("/notif_user")   
    @Produces(MediaType.APPLICATION_JSON)     
    NotifUserDto getNotifUser(@HeaderParam("lang")String lang);
    
    @PUT
    @Path("/notif_user/config")   
    @Produces(MediaType.APPLICATION_JSON)     
    NotifUserDto updateNotifUser(NotifUserDto dto,@HeaderParam("lang")String lang);    

    @GET
    @Path("/notification/{id}")   
    @Produces(MediaType.APPLICATION_JSON)     
    NotifDto getNotifById(@PathParam("id")Long id,@HeaderParam("lang")String lang);

    
    @DELETE
    @Path("/notification/{id}")   
    Response deleteNotifById(@PathParam("id")Long id,@HeaderParam("lang")String lang);    
    
    @POST
    @Path("/invite/send_folder_invite")
    @Produces(MediaType.APPLICATION_JSON)            
    NotifDto sendFolderInvite(FolderInviteDto dto);
    
    @PUT
    @Path("/notification/{id}/confirm_notification")
    @Produces(MediaType.APPLICATION_JSON)            
    NotifDto confirmNotification(@PathParam("id")Long id,@HeaderParam("lang")String lang);    
}
