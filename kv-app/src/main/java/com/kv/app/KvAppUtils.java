/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app;

import com.kv.entry.NamedAnn;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import jakarta.enterprise.inject.spi.CDI;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

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
        Button cancelButton = new Button(getResString("word.cancel"), e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.setWidthFull();
        return dialog;  
    }

    public static Dialog getBasicDialog(String title,Component content,ComponentEventListener<ClickEvent<Button>> clickListener) {
        Dialog dialog = getBasicDialog(title, content);
        Button confirmButton = new Button(getResString("word.confirm"),e->{
        clickListener.onComponentEvent(e);
        dialog.close();
        });
        dialog.getFooter().add(confirmButton);
        return dialog;
    }
    
    public static Object getLocalizedList(){
        return null;
    }
    
    public static ResourceBundle getResBundle() {
        Locale locale = CDI.current().select(Locale.class,new NamedAnn("subjLocale")).get();            
        return ResourceBundle.getBundle("KvAppWords",locale);
    }
    
    public static String getResString(String key){
        return getResBundle().getString(key);
    }
    
    public static List<String> getResArray(String key){
        Locale locale = CDI.current().select(Locale.class,new NamedAnn("subjLocale")).get();            
        String csv = ResourceBundle.getBundle("KvAppWords",locale).getString(key);
        String[] vals = csv.split(",");
        return List.of(vals);
    }
    
    public static Icon getVaadinIcon(VaadinIcon icon){
        Icon result = icon.create();
        result.getElement().addEventListener("click", e->{}).addEventData("event.stopPropagation()");
        return result;
    }
}
