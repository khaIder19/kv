/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.dao;

import com.kv.entry.model.Folder;
import com.kv.entry.model.FolderPermissionType;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author k-iderr
 */
@ApplicationScoped
public class FolderDao implements PanacheRepository<Folder>{
  
    public List<Folder> findByUserIdAndPermissionType(UUID userId,FolderPermissionType permissionType,Page page){     
        return find("SELECT DISTINCT f FROM Folder f "
                + "LEFT JOIN FETCH f.permissions fp "
                + "WHERE fp.user.id = ?1 AND fp.permissionType = ?2", userId,permissionType).page(page).list();
    }
    
    public List<Folder> findByUserId(UUID userId,Page page){       
        return find("SELECT DISTINCT f FROM Folder f "
                + "LEFT JOIN FETCH f.permissions fp "
                + "WHERE fp.user.id = ?1", userId).page(page).list();
    }
    
    public Long deleteByIds(List<Long> ids){
        return delete("DELETE f FROM Folder f WHERE id IN ?1", ids);
    }
}
