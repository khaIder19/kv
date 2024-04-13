/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.dao;

import com.kv.entry.model.EntryUser;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

/**
 *
 * @author k-iderr
 */
@ApplicationScoped
public class EntryUserDao implements PanacheRepositoryBase<EntryUser, UUID>{
    
}
