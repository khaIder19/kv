/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.dao;

import com.kv.entry.model.FolderPermission;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

/**
 *
 * @author k-iderr
 */
@ApplicationScoped
public class FolderPermissionDao implements PanacheRepository<FolderPermission>{
    public FolderPermission getByFolderAndUser(Long folderId,UUID userId){
        return find("SELECT fp FROM FolderPermission fp "
                + "WHERE fp.folder.id = ?1 AND fp.user.id = ?2", folderId,userId).singleResult();
    }  
}
