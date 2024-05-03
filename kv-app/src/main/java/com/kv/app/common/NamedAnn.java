/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Named;

/**
 *
 * @author k-iderr
 */
public class NamedAnn extends AnnotationLiteral<Named> implements Named{

    private String value;
    
    public NamedAnn(String value){
        this.value = value;
    }
    
    @Override
    public String value() {
        return value;
    }
    
}
