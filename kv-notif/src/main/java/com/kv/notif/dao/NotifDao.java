/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.notif.dao;

import com.kv.notif.model.Notif;
import com.kv.notif.model.NotifStatus;
import com.kv.notif.model.NotifType;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 *
 * @author k-iderr
 */
@ApplicationScoped
public class NotifDao implements PanacheRepository<Notif>{
    
    @ConfigProperty(name = "kv.notif.max-inactive-notifs",defaultValue = "10")
    Integer maxInactiveNotifs;
    
    public List<Notif> findByUserIdAndNotifType(UUID userId,NotifType notifType,Page page){     
        return find("SELECT n FROM Notif n "
                + "WHERE n.user.id = ?1 AND n.notifType = ?2 ORDER BY n.createdAt DESC", userId,notifType).page(page).list();
    }
    
    
    public List<Notif> findByUserId(UUID userId,Page page){       
        return find("SELECT n FROM Notif n "
                + "WHERE n.user.id = ?1 ORDER BY n.createdAt DESC", userId).page(page).list();
    }
    
    public Notif findByIdAndUserId(Long id,UUID userId){
        return find("SELECT n FROM Notif n WHERE n.id = ?1 AND n.user.id = ?2",id,userId).singleResult();
    }
    
    public boolean isNotifAlreadySentByFolderId(UUID toUserId,Long folderId){
        List<Notif> result = find("SELECT n FROM Notif n "
                + "WHERE n.user.id = ?1 AND n.notifType = ?2 AND n.notifStatus = ?3",
                toUserId,NotifType.INVITE,NotifStatus.CREATED).list();
        return result.stream().filter(n->{
         Object invFoldId = n.getProps().get("invite_folder_id");
         return invFoldId != null && invFoldId.toString().equals(folderId.toString());
        }).count() > 0;
    }
    
    public Notif createAndDeleteLastInactive(Notif notif){
        Notif lastInactive = getLastInactiveNotifByUser(notif.getUser().getId());
        if(lastInactive != null)delete(lastInactive);
        persist(notif);
        return notif;        
    }
    
    public Notif getLastInactiveNotifByUser(UUID userUuid){
        List<Notif> lastNotifs = find("SELECT n FROM Notif n WHERE n.user.id = ?1 "
                + "AND n.notifStatus = ?2 "
                + "ORDER BY n.createdAt ASC",userUuid,NotifStatus.IDLE)
                .page(0, maxInactiveNotifs).list();
        if(lastNotifs.size() == maxInactiveNotifs){
            return lastNotifs.get(0);
        }else{
            return null;
        }
    }
    
}
