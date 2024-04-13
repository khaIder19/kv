/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.asp.event;

import com.kv.entry.dao.FolderPermissionDao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 *
 * @author k-iderr
 */
@ApplicationScoped
public class FolderPermissionEvServ {
    
    @Inject
    FolderPermissionDao fpDao; 
    
    
    public void precessPermissionAccept(Object invite){
    }
    
}
