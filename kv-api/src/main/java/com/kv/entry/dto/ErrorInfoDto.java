/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kv.entry.asp.i18n.I18nSerializer;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author k-iderr
 */
public class ErrorInfoDto {
    
    private int status;
    private String code;
    private String field;
    @JsonSerialize(using = I18nSerializer.class)
    private String message;
    
    public ErrorInfoDto(){
        
    }

    public ErrorInfoDto(String code, String message) {
        this(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),code,null,message);
    }    
    
    public ErrorInfoDto(String code, String field, String message) {
        this(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),code,field,message);
    }    
    
    public ErrorInfoDto(int status, String code, String field, String message) {
        this.status = status;
        this.code = code;
        this.field = field;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorInfoDto{" + "status=" + status + ", code=" + code + ", field=" + field + ", message=" + message + '}';
    }
      
}
