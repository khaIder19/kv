/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.notif.dao;

import com.kv.notif.model.NotifUser;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

/**
 *
 * @author k-iderr
 */
@ApplicationScoped
public class NotifUserDao implements PanacheRepositoryBase<NotifUser, UUID>{
    
}
