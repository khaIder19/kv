/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.rest;

import com.kv.entry.asp.sec.FolderRoleAllowed;
import com.kv.entry.dao.EntryDao;
import com.kv.entry.dao.FolderDao;
import com.kv.entry.dto.BatchIdObjDto;
import com.kv.entry.model.Entry;
import com.kv.entry.model.Folder;
import com.kv.entry.model.FolderPermissionType;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
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
import com.kv.entry.asp.event.DaoEvent;
import com.kv.entry.asp.event.DaoEventParam;

/**
 *
 * @author k-iderr
 */
@Path("/api/folder/{folderId}/entry")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class EntryRes {
    
    @Inject
    private EntryDao dao;
    
    @Inject
    private FolderDao efDao;
    
    @GET
    @FolderRoleAllowed(FolderPermissionType.READ)    
    public Response getByFolderId(@PathParam("folderId")Long folderId,@QueryParam("page") Integer index,
            @QueryParam("size") Integer size,@QueryParam("count")boolean isCount){
        Object result = null;
        Page page = index != null && size != null ? Page.of(index, size) : null;
        if(isCount){
            result = dao.queryByFolderId(folderId, page).count();
        }else{
            result = dao.findByFolderId(folderId,page);            
            Log.debugf("With size : %s", ((List)result).size());     
        }   
        return Response.ok(result).build();
    }
    
    @Path("/{id}")
    @GET
    @FolderRoleAllowed(FolderPermissionType.READ)    
    public Entry getById(@PathParam("id")Long id){
        Entry e = dao.findById(id);
        if(e == null) throw new NotFoundException();
        return e;
    }
    
    @Transactional
    @Path("/{id}")
    @DELETE
    @FolderRoleAllowed(FolderPermissionType.WRITE)
    @DaoEventParam(method = "DELETE",type = Entry.class)    
    @DaoEvent
    public Response delete(@PathParam("id")Long id){
        Entry entry = dao.findById(id);
        if(entry == null) throw new NotFoundException();
        dao.deleteById(entry.getId());      
        return Response.ok(entry.asPojo()).build();
    }
    
    @Transactional
    @Path("/deleteByIds")
    @POST
    @FolderRoleAllowed(FolderPermissionType.WRITE)
    public void deleteByIds(@PathParam("folderId")Long fId,BatchIdObjDto<Long,Void> dto){
        dao.deleteByIds(fId,dto.getIds());
    }
    
    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @FolderRoleAllowed(FolderPermissionType.WRITE) 
    @DaoEventParam(method = "CREATE",type = Entry.class)    
    @DaoEvent   
    public Response create(@PathParam("folderId")Long fId,Entry e){
        Folder ef = efDao.findById(fId);
        if(ef == null) throw new NotFoundException();
        Log.debugf("With key: %s & value: %s", e.getKey(),e.getValue());        
        Entry entry = new Entry(ef,e.getKey(), e.getValue());
        dao.persist(entry);
        return Response.created(URI.create("/entry/"+entry.getId())).entity(entry).build();
    }
    
    @Transactional
    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)    
    @FolderRoleAllowed(FolderPermissionType.WRITE)
    @DaoEventParam(method = "UPDATE",type = Entry.class)    
    @DaoEvent
    public Response update(@PathParam("id")Long id,Entry e){
        Entry entry = dao.findById(id);
        if(entry != null){
            Log.debugf("With key: %s & value: %s", e.getKey(),e.getValue());
            entry.setKey(e.getKey());
            entry.setValue(e.getValue());
            return Response.ok(entry.asPojo()).build();
        }else{
            throw new NotFoundException();
        }
    }
}
