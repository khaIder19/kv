/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.view.entry.client;

import com.kv.app.view.entry.data.EntryDto;
import com.kv.app.view.entry.data.FolderDto;
import com.kv.app.exc.BaseRestExcMapper;
import io.quarkus.oidc.token.propagation.AccessTokenRequestFilter;
import jakarta.ws.rs.Consumes;
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
public interface EntryResClient {
    
    @GET
    @Path("/folder")       
    List<FolderDto> getAllFoldersByUser(@QueryParam("permissionType")String fPermType,@QueryParam("page")Integer page,
            @QueryParam("size")Integer size,@HeaderParam("lang")String lang);
    
    @GET
    @Path("/folder/{folderId}")   
    @Produces(MediaType.APPLICATION_JSON)     
    FolderDto getFolderById(@PathParam("folderId")Long folderId,@HeaderParam("lang")String lang);
    
    @DELETE
    @Path("/folder/{folderId}")   
    Response deleteFolderById(@PathParam("folderId")Long id,@HeaderParam("lang")String lang);
    
    @PUT
    @Path("/folder/{folderId}")   
    Response upadeFolderById(@PathParam("folderId")Long id,FolderDto folderDto,@HeaderParam("lang")String lang);    
    
    @DELETE
    @Path("/folder/{folderId}/folder_permission/{permissionId}")   
    Response deleteFolderPermissionById(@PathParam("folderId")Long folderId,
            @PathParam("permissionId")Long id,@HeaderParam("lang")String lang);    
    
    @POST
    @Path("/folder")    
    @Produces(MediaType.APPLICATION_JSON)
    FolderDto createFolder(FolderDto dto,@HeaderParam("lang")String lang);
    
    @GET
    @Path("/folder/{folderId}/entry")   
    @Produces(MediaType.APPLICATION_JSON)     
    List<EntryDto> getAllEntryByFolderId(@PathParam("folderId")Long folderId,
            @QueryParam("page")Integer page,@QueryParam("size")Integer size,
            @QueryParam("count")Boolean count,@HeaderParam("lang")String lang);    
    
    @DELETE
    @Path("/folder/{folderId}/entry/{entryId}")   
    @Produces(MediaType.APPLICATION_JSON)     
    Response deleteEntryById(@PathParam("folderId")Long folderId,
            @PathParam("entryId")Long entryId,@HeaderParam("lang")String lang); 
    
    @POST
    @Path("/folder/{folderId}/entry")   
    @Produces(MediaType.APPLICATION_JSON)     
    EntryDto createEntry(@PathParam("folderId")Long folderId,
            EntryDto entryDto,@HeaderParam("lang")String lang);
    
    @PUT
    @Path("/folder/{folderId}/entry/{entryId}")   
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)        
    Response updateEntry(@PathParam("folderId")Long folderId,
            @PathParam("entryId")Long entryId,EntryDto entryDto,@HeaderParam("lang")String lang);    
}
