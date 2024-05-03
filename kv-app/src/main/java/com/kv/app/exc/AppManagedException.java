/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.exc;

/**
 *
 * @author k-iderr
 */
public class AppManagedException extends RuntimeException{

    
    
    public AppManagedException(String message) {
        super(message);
    }

    public AppManagedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppManagedException(Throwable cause) {
        super(cause);
    }
  
}
