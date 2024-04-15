/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.view.entry.client;

import com.kv.app.view.entry.data.EntryDto;
import com.kv.app.view.entry.data.FolderDto;
import com.kv.app.exc.BaseRestExcMapper;
import com.kv.app.view.entry.data.EntryUserDto;
import io.quarkus.oidc.token.propagation.AccessTokenRequestFilter;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
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
public interface EntryResClient {
    
    @GET
    @Path("/folder")       
    List<FolderDto> getAllFoldersByUser(@QueryParam("permissionType")String fPermType,@QueryParam("page")Integer page,
            @QueryParam("size")Integer size);
    
    @GET
    @Path("/folder/{folderId}")   
    @Produces(MediaType.APPLICATION_JSON)     
    FolderDto getFolderById(@PathParam("folderId")Long folderId);
    
    @DELETE
    @Path("/folder/{folderId}")   
    Response deleteFolderById(@PathParam("folderId")Long id);
    
    @PUT
    @Path("/folder/{folderId}")   
    Response upadeFolderById(@PathParam("folderId")Long id,FolderDto folderDto);    
    
    @DELETE
    @Path("/folder/{folderId}/folder_permission/{permissionId}")   
    Response deleteFolderPermissionById(@PathParam("folderId")Long folderId,@PathParam("permissionId")Long id);    
    
    @POST
    @Path("/folder")    
    @Produces(MediaType.APPLICATION_JSON)
    FolderDto createFolder(FolderDto dto);
    
    @GET
    @Path("/folder/{folderId}/entry")   
    @Produces(MediaType.APPLICATION_JSON)     
    List<EntryDto> getAllEntryByFolderId(@PathParam("folderId")Long folderId,
            @QueryParam("page")Integer page,@QueryParam("size")Integer size,
            @QueryParam("count")Boolean count);    
    
    @DELETE
    @Path("/folder/{folderId}/entry/{entryId}")   
    @Produces(MediaType.APPLICATION_JSON)     
    Response deleteEntryById(@PathParam("folderId")Long folderId,@PathParam("entryId")Long entryId); 
    
    @POST
    @Path("/folder/{folderId}/entry")   
    @Produces(MediaType.APPLICATION_JSON)     
    EntryDto createEntry(@PathParam("folderId")Long folderId,EntryDto entryDto);
    
    @PUT
    @Path("/folder/{folderId}/entry/{entryId}")   
    @Produces(MediaType.APPLICATION_JSON)     
    EntryDto updateEntry(@PathParam("folderId")Long folderId,@PathParam("entryId")Long entryId,EntryDto entryDto);    
}
