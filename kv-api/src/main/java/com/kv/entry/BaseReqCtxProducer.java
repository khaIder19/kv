/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry;

import com.kv.entry.dao.EntryUserDao;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.HttpHeaders;
import java.util.Locale;
import java.util.UUID;
import org.eclipse.microprofile.jwt.JsonWebToken;

/**
 *
 * @author k-iderr
 */
@RequestScoped
public class BaseReqCtxProducer {
 
    
    private JsonWebToken jwt;
    private Locale userLocale;
    private UUID subjId;
    
    @Inject
    EntryUserDao dao;
    
    
    
    @Inject
    public BaseReqCtxProducer(JsonWebToken webToken,HttpHeaders headers){
        jwt = webToken; 
        String subClaim = jwt.getClaim("sub").toString();
        
        subjId = UUID.fromString(subClaim);
        
        userLocale = Locale.US;
        String lang = headers.getHeaderString("lang");
        if(lang != null && lang.equals("it")){
            userLocale = Locale.ITALY;
        }
    }
    
    @Produces
    @Named("subjUUID")
    protected UUID getPrincipalId(){
        return subjId;
    }
    
    @Produces
    @Named("subjLocale")    
    protected Locale getPrincipalLocale(){
        return userLocale;
    }
    
    @Produces
    @Named("subjName")    
    protected String getPrincipalName(){
        return jwt.getClaim("preferred_username");
    }     
}
