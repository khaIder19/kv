/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.exc;

import com.kv.app.KvAppUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;
import io.quarkus.logging.Log;

/**
 *
 * @author k-iderr
 */
public class BaseExcMapper implements ErrorHandler{

    @Override
    public void error(ErrorEvent ee) {
        Throwable cause = ee.getThrowable();
        boolean isManaged = cause instanceof AppManagedException;
        if(!isManaged){
            Log.error("Unmanaged error ocurred", cause);
        }
        
        if(UI.getCurrent() != null) {
            UI.getCurrent().access(() -> {
                Notification n = new Notification();
                n.setText(isManaged ? cause.getMessage() : getDefMessage());
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                n.setDuration(2000);
                n.open();
            });
        }
    }
    
    private String getDefMessage(){
        return KvAppUtils.getResString("exc.error.generic");
    }
    
}
