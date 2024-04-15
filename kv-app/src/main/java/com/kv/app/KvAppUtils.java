/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 *
 * @author k-iderr
 */
public class KvAppUtils {
    public static Dialog getBasicDialog(String title,Component content){
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(title);
        if(content != null){
        dialog.add(content);
        }
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.setWidthFull();
        return dialog;  
    }

    public static Dialog getBasicDialog(String title,Component content,ComponentEventListener<ClickEvent<Button>> clickListener) {
        Dialog dialog = getBasicDialog(title, content);
        Button confirmButton = new Button("Confirm",e->{
        clickListener.onComponentEvent(e);
        dialog.close();
        });
        dialog.getFooter().add(confirmButton);
        return dialog;
    }
}
