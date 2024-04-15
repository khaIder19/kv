/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.exc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author k-iderr
 */
public class ErrorDto {
    private List<ErrorInfoDto> errors;
    
    public List<ErrorInfoDto> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorInfoDto> errors) {
        this.errors = errors;
    }
    
    public void addError(ErrorInfoDto error){
        if(errors == null){
            errors = new ArrayList<>();
        }
        errors.add(error);
    }

    @Override
    public String toString() {
        return Arrays.toString(errors.toArray());
    }  
}
