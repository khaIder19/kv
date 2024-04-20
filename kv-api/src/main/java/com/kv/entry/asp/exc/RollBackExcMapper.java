/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.asp.exc;

import com.kv.entry.dto.ErrorDto;
import com.kv.entry.dto.ErrorInfoDto;
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
public class RollBackExcMapper implements ExceptionMapper<ConstraintViolationException>{

    @Override
    public Response toResponse(ConstraintViolationException e) {     
        ErrorDto errorDto = new ErrorDto();
        int status = Response.Status.BAD_REQUEST.getStatusCode();
           ErrorInfoDto errorInfo = new ErrorInfoDto(status,
                    ConstraintViolationException.class.getSimpleName(), null,"exc.constr.db."+e.getConstraintName());
            errorDto.addError(errorInfo);        
        return Response.status(status).entity(errorDto).type(MediaType.APPLICATION_JSON).build();
    }
    
}
