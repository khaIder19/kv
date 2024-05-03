/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.asp.sec;

import com.kv.entry.dao.FolderPermissionDao;
import com.kv.entry.model.FolderPermission;
import com.kv.entry.model.FolderPermissionType;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;
import org.eclipse.microprofile.jwt.JsonWebToken;

/**
 *
 * @author k-iderr
 */
@Provider
public class AppAuthFilter implements ContainerRequestFilter{

    @Inject
    private JsonWebToken jwt;
    
    @Inject
    private FolderPermissionDao folderPermissionDao;    
    
    @Context
    private ResourceInfo resourceInfo;
    
 
    private UUID getPrincipalId(){
        String subClaim = jwt.getClaim("sub").toString();
        return UUID.fromString(subClaim);
    }
    
    @Override
    public void filter(ContainerRequestContext crc) throws IOException {
        Method method = resourceInfo.getResourceMethod();
        if(method.isAnnotationPresent(FolderRoleAllowed.class)){
            FolderRoleAllowed folderRole = method.getAnnotation(FolderRoleAllowed.class);
            
            Long folderId = Long.valueOf(
                    crc.getUriInfo().getPathParameters().getFirst("folderId"));
            FolderPermission folderPermission = folderPermissionDao
                    .getByFolderAndUser(folderId, getPrincipalId());
            
            if(folderPermission == null || !checkFolderRole(folderRole, folderPermission)){
                throw new ForbiddenException();
            }
        }
    }
    
    private boolean checkFolderRole(FolderRoleAllowed role,FolderPermission permission){
           if(!(permission.getPermissionType() == FolderPermissionType.OWNER)){
               if(permission.getPermissionType() == FolderPermissionType.WRITE &&
                       role.value()[0] == FolderPermissionType.READ){
                  return true; 
               }
               return role.value()[0] == permission.getPermissionType();
           }
           return true;
    }
    
}
