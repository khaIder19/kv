/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.dao;

import com.kv.entry.model.Entry;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 *
 * @author k-iderr
 */
@ApplicationScoped
public class EntryDao implements PanacheRepository<Entry>{
    
    public List<Entry> findByFolderId(Long folderId,Page page){
        return queryByFolderId(folderId, page).list();
    }
    
    public PanacheQuery<Entry> queryByFolderId(Long folderId,Page page){
        return find("SELECT e FROM Entry e WHERE folder.id = ?1 ORDER BY e.createdAt DESC",folderId).page(page);
    }    
    
    public Long deleteByIds(Long folderId,List<Long> ids){
        return delete("DELETE e FROM Entry e WHERE e.id IN ?1 AND e.folder.id = ?2",
                ids,folderId);
    }

}
