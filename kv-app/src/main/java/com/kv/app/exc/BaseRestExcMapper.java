/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.exc;

import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

/**
 *
 * @author k-iderr
 */
public class BaseRestExcMapper implements ResponseExceptionMapper<AppManagedException>{

    @Override
    public AppManagedException toThrowable(Response rspns) {
        ErrorDto errorDto = (ErrorDto) rspns.readEntity(ErrorDto.class);               
        String finalErrorMessage = "";
        for(ErrorInfoDto errInfo : errorDto.getErrors()){
            finalErrorMessage = finalErrorMessage.concat(" - ").concat(errInfo.getMessage()).concat("\n");
        }
        
        return new AppManagedException(finalErrorMessage);
    }
    
}
