/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.exc;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;
import io.quarkus.logging.Log;
import jakarta.ws.rs.WebApplicationException;

/**
 *
 * @author k-iderr
 */
public class BaseExcMapper implements ErrorHandler{

    @Override
    public void error(ErrorEvent ee) {
        Throwable cause = ee.getThrowable();
                Log.info("",cause);
        if(UI.getCurrent() != null) {
            UI.getCurrent().access(() -> {
                Notification.show(cause instanceof AppManagedException ? cause.getMessage() : getDefMessage());
            });
        }
    }
    
    private String getDefMessage(){
        return "An internal error has occurred." +
                        "Please contact support.";
    }
    
}
