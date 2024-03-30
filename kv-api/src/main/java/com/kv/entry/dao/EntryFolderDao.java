/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.dao;

import com.kv.entry.model.EntryFolder;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author k-iderr
 */
public class EntryFolderDao implements PanacheRepository<EntryFolder>{
    
    public List<EntryFolder> findByUserId(UUID userId){
        return find("SELECT DISTINCT ef FROM EntryFolder ef"
                + "LEFT JOIN FolderPermission fp ON ef.id = fp.folder_id"
                + "WHERE ef.owner_id = ?1 OR fp.user_id = ?1", userId).list();
    }
}
