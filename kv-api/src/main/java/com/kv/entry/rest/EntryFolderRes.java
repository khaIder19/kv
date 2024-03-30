/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.rest;

import com.kv.entry.dao.EntryFolderDao;
import com.kv.entry.model.EntryFolder;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.UUID;

/**
 *
 * @author k-iderr
 */
@Path("/folder")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class EntryFolderRes {
       
    @Inject
    private EntryFolderDao dao;

    
    public EntryFolderRes() {
    }
    
 
    @Path("/")
    @GET
    public Response getAllByUser(){
        return Response.ok(dao.findByUserId(UUID.randomUUID())).build();
    }
    
    
    @Path("/{id}")
    @GET
    public Response getById(@PathParam("id")Long id){
        EntryFolder ef = dao.findById(id);
        if(ef != null){
            return Response.ok(ef).build();
        }else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    
    @Path("/{id}")
    @DELETE
    public Response delete(@PathParam("id")Long id){
        if(dao.deleteById(id)){
            return Response.ok().build();   
        }else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    @Transactional
    @Path("/")
    @POST
    public Response create(EntryFolder ef){
        EntryFolder entryFolder = new EntryFolder(ef.getName(),UUID.randomUUID());
        dao.persist(entryFolder);
        return Response.created(URI.create("/entry/"+entryFolder.getId())).build();
    }
    
    @Transactional
    @Path("/{id}")
    @PUT
    public Response update(@PathParam("id")Long id,EntryFolder ef){
        EntryFolder entryFolder = dao.findById(id);
        if(entryFolder != null){
            entryFolder.setName(ef.getName());
            return Response.ok(entryFolder).build();
        }else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
}
