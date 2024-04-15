/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.exc;

import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

/**
 *
 * @author k-iderr
 */
public class BaseRestExcMapper implements ResponseExceptionMapper<AppManagedException>{

    @Override
    public AppManagedException toThrowable(Response rspns) {
        ErrorDto errorDto = (ErrorDto) rspns.readEntity(ErrorDto.class);               
        return new AppManagedException(Arrays.toString(errorDto.getErrors().toArray()));
    }
    
}
