/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.asp.exc;

import com.kv.entry.dto.ErrorDto;
import com.kv.entry.dto.ErrorInfoDto;
import io.quarkus.logging.Log;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author k-iderr
 */
@Provider
public class BaseExcMapper implements ExceptionMapper<Exception>{
    
    @Override
    public Response toResponse(Exception e){
        Throwable cause = e;
        while(cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        
        if(cause instanceof ConstraintViolationException){
            return new RollBackExcMapper().toResponse((ConstraintViolationException) cause);
        }else if(cause instanceof jakarta.validation.ConstraintViolationException){
            return new ConstrExcMapper().toResponse((jakarta.validation.ConstraintViolationException) cause);
        }
        
        
        Log.error("Unhandled exception thrown", e);
        ErrorDto errorDto = new ErrorDto();
        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();                
        ErrorInfoDto errorInfoDto = new ErrorInfoDto(e.getClass().getSimpleName(),"exc.web.status.500");
        errorDto.addError(errorInfoDto);
        return Response.status(status).entity(errorDto).type(MediaType.APPLICATION_JSON).build();
    }
             
}
