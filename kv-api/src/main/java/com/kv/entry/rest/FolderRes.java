/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.rest;

import com.kv.entry.asp.sec.FolderRoleAllowed;
import com.kv.entry.dao.EntryUserDao;
import com.kv.entry.dao.FolderDao;
import com.kv.entry.dao.FolderPermissionDao;
import com.kv.entry.model.EntryUser;
import com.kv.entry.model.Folder;
import com.kv.entry.model.FolderPermission;
import com.kv.entry.model.FolderPermissionType;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.ForbiddenException;
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

/**
 *
 * @author k-iderr
 */
@Path("/api/folder")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class FolderRes extends BaseRes{
       
    @Inject
    private FolderDao dao;
    
    @Inject
    private FolderPermissionDao folderPermissionDao;
    
    @Inject
    private EntryUserDao userDao;    
 
    @GET
    public Response getAllByUser(@QueryParam("permissionType")FolderPermissionType fPermType,@QueryParam("page")@DefaultValue("0") Integer page,
            @QueryParam("size")@DefaultValue("50") Integer size){
        List<Folder> resultList = List.of();
        if(fPermType != null){
            resultList = dao.findByUserIdAndPermissionType(getPrincipalId(), fPermType,Page.of(page, size));
        }else{
            resultList = dao.findByUserId(getPrincipalId(),Page.of(page, size));
        }
        Log.debugf("getAllByUser found list with size : %s", resultList.size());
        return Response.ok(resultList).build();
    }
    
    
    @GET
    @Path("/{folderId}")
    @FolderRoleAllowed(FolderPermissionType.READ)
    public Response getById(@PathParam("folderId")Long id){           
        Folder ef = dao.findById(id);
        if(ef == null) throw new NotFoundException();
        return Response.ok(ef).build();
    }
    
    
    @Transactional
    @Path("/{folderId}")
    @DELETE
    @FolderRoleAllowed(FolderPermissionType.OWNER)
    public Response delete(@PathParam("folderId")Long id){
        Folder ef = dao.findById(id);
        if(ef == null) throw new NotFoundException();
        dao.deleteById(ef.getId());
        return Response.ok().build();
    }
    
    @Transactional
    @Path("/{folderId}/folder_permission/{folderPermissionId}")
    @DELETE
    @FolderRoleAllowed(FolderPermissionType.READ)
    public Response unsub(@PathParam("folderId")Long folderId,@PathParam("folderPermissionId")Long id){
        FolderPermission fp = folderPermissionDao.findById(id);
        if(fp == null) throw new NotFoundException();
        
        if(fp.getUser().getId().equals(getPrincipalId())) throw new BadRequestException("exc.folder-permission.act.delete.permission_of_owner");

        //check if is owner or user of the permission
        if(!fp.getFolder().getOwner().getId().equals(getPrincipalId())){
            if(!fp.getUser().getId().equals(getPrincipalId())){
                throw new ForbiddenException("exc.folder-permission.act.delete.not_owner_of_permission");
            }
        }
        
        folderPermissionDao.delete(fp);
        return Response.ok().build();
    }

    @Transactional
    @Path("/{folderId}/folder_permission/{folderPermissionId}")
    @PUT
    @FolderRoleAllowed(FolderPermissionType.OWNER)
    public Response updateFolderPermission(@PathParam("folderPermissionId")Long id,FolderPermission fp){
        FolderPermission folderPermission = folderPermissionDao.findById(id);
        if(folderPermission == null) throw new NotFoundException();     
        
        if(fp.getUser().getId().equals(getPrincipalId()))
        throw new BadRequestException("exc.folder-permission.act.update.permission_of_owner");
        
        folderPermission.setPermissionType(fp.getPermissionType());        
        return Response.ok().build();
    }    
    
    @Transactional
    @POST
    public Response create(Folder ef){
        
        EntryUser entryUser = userDao.findById(getPrincipalId());
        if(entryUser == null){
            entryUser = new EntryUser(getPrincipalId(),"usreName");
            userDao.persist(entryUser);
            Log.debug("EntryUser first created");            
        }
        
        Folder entryFolder = new Folder(ef.getName(),entryUser);
        dao.persist(entryFolder);
        FolderPermission ownerPermission = new FolderPermission(entryUser,
                entryFolder, FolderPermissionType.OWNER);
        folderPermissionDao.persist(ownerPermission);
        return Response.created(URI.create("/folder/"+entryFolder.getId())).entity(entryFolder).build();
    }
    
    @Transactional
    @Path("/{folderId}")
    @PUT
    @FolderRoleAllowed(FolderPermissionType.OWNER)    
    public Response update(@PathParam("id")Long id,Folder ef){
        Folder entryFolder = dao.findById(id);
        if(entryFolder != null){
            entryFolder.setName(ef.getName());
            return Response.ok(entryFolder).build();
        }else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
}
