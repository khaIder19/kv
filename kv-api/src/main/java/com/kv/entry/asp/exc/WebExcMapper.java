/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.asp.exc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kv.entry.dto.ErrorDto;
import com.kv.entry.dto.ErrorInfoDto;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.ServiceUnavailableException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 *
 * @author k-iderr
 */
@Provider
public class WebExcMapper implements ExceptionMapper<WebApplicationException>{
    
    @Inject
    ObjectMapper mapper;
    
    @Override
    public Response toResponse(WebApplicationException e) {
        if(e instanceof ServiceUnavailableException){
            Log.error("Some services are not available", e);
        }
        
        ErrorDto errorDto = new ErrorDto();
        ErrorInfoDto errorInfoDto = null;              
        int status = e.getResponse().getStatus();
        errorInfoDto = new ErrorInfoDto(status,
                    e.getClass().getSimpleName(),null,e.getMessage());
        errorDto.addError(errorInfoDto);
        return Response.status(status).entity(errorDto).type(MediaType.APPLICATION_JSON).build();
    }    
}
