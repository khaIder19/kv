/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.asp.exc;

import com.kv.entry.dto.ErrorDto;
import com.kv.entry.dto.ErrorInfoDto;
import io.quarkus.logging.Log;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 *
 * @author k-iderr
 */
//@Provider
public class BaseExcMapper implements ExceptionMapper<Exception>{

    @Override
    public Response toResponse(Exception e) {
        ErrorDto errorDto = new ErrorDto();
        ErrorInfoDto errorInfoDto = null;
        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        if(e instanceof WebApplicationException){
            WebApplicationException we = (WebApplicationException) e;
            status = we.getResponse().getStatus();
            errorInfoDto = new ErrorInfoDto(status,
                    we.getClass().getSimpleName(),null,we.getLocalizedMessage());
        }else{
            errorInfoDto = new ErrorInfoDto(e.getClass().getSimpleName(),e.getLocalizedMessage());            
        }
        errorDto.addError(errorInfoDto);
        return Response.status(status).entity(errorDto).build();
    }
    
}
