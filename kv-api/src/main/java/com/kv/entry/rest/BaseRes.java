/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;
import java.util.UUID;
import org.eclipse.microprofile.jwt.JsonWebToken;

/**
 *
 * @author k-iderr
 */
public class BaseRes {
    
    @Inject
    private JsonWebToken jwt;
    
    protected UUID getPrincipalId(){
        String subClaim = jwt.getClaim("sub").toString();
        return UUID.fromString(subClaim);
    }
    
}
