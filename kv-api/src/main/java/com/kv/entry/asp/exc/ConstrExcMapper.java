/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.asp.exc;

import com.kv.entry.dto.ErrorDto;
import com.kv.entry.dto.ErrorInfoDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 *
 * @author k-iderr
 */
@Provider
public class ConstrExcMapper implements ExceptionMapper<ConstraintViolationException>{

    private static final int status = Response.Status.BAD_REQUEST.getStatusCode();
    
    @Override
    public Response toResponse(ConstraintViolationException e) {
        ErrorDto errorDto = new ErrorDto();
            for(ConstraintViolation cv : e.getConstraintViolations()){
                errorDto.addError(getConstraintViolationErrorDto(cv));
            }
        return Response.status(status).entity(errorDto).type(MediaType.APPLICATION_JSON).build();
    }
    
    private ErrorInfoDto getConstraintViolationErrorDto(ConstraintViolation cv){
        ErrorInfoDto errorInfoDto = new ErrorInfoDto(status,cv.getClass().getSimpleName(),
        cv.getPropertyPath().toString(),cv.getMessage());
        return errorInfoDto;
    }
}
