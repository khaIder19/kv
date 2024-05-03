/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.common;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.Locale;
import java.util.UUID;
import org.eclipse.microprofile.jwt.JsonWebToken;

/**
 *
 * @author k-iderr
 */
@SessionScoped
public class BaseReqCtxProducer {
 
    @Inject
    UserSessionParamRegistry userSessionParamRegistry;
    
    private JsonWebToken jwt;
    private UUID subjId; 
    
    @Inject
    public BaseReqCtxProducer(JsonWebToken webToken){
        jwt = webToken; 
        String subClaim = jwt.getClaim("sub").toString();
        
        subjId = UUID.fromString(subClaim);
    }  
    
    
    private Locale getLocaleFromClaimString(String claim){
        Locale locale = Locale.US;
        if(claim != null && claim.equals("it")){
            locale = Locale.ITALY;
        }
        return locale;
    }
    
    @PostConstruct
    public void init(){
        UserSessionParamDto userParam = new UserSessionParamDto(subjId,
                jwt.getClaim("preferred_username"),
                jwt.getClaim("locale"));
        userSessionParamRegistry.addUserSessionParam(userParam);
    }
    
    @PreDestroy
    public void close(){
        userSessionParamRegistry.removeSessionParamForUser(subjId);
    }
    
    @Produces
    @Named("subjUUID")
    protected UUID getPrincipalId(){
        return subjId;
    }
    
    @Produces
    @Named("subjLocale")    
    protected Locale getPrincipalLocale(){
        UserSessionParamDto userSessionParamDto = userSessionParamRegistry.getByUserId(subjId);
        return getLocaleFromClaimString(userSessionParamDto.getLocale());
    }
    
    @Produces
    @Named("subjLang")    
    protected String getPrincipalLang(){
        return userSessionParamRegistry.getByUserId(subjId).getLocale();
    }    

    
    @Produces
    @Named("subjName")    
    protected String getPrincipalName(){
        return userSessionParamRegistry.getByUserId(subjId).getUsername();
    }     
}
