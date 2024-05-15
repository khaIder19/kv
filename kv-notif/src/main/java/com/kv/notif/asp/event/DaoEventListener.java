/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.notif.asp.event;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author k-iderr
 */
@Interceptor
@DaoEvent
public class DaoEventListener {
    
    @Inject
    DaoCrudEvProducer evProducer;
    
    @AroundInvoke
    public Object publishDaoEvent(InvocationContext ctx) throws Exception{
        DaoEventParam methodInfo = ctx.getMethod()
                .getAnnotation(DaoEventParam.class);
        
        String crudMethod = methodInfo.method();
        String type = methodInfo.type().getSimpleName();
        
        Object result = ctx.proceed();
        
        if(result != null){
            if(result instanceof Response){
                Response response = (Response) result;
                if(response.getStatus() == 200 || response.getStatus() == 201){
                evProducer.publishDaoEvent(crudMethod,type,response.getEntity());                  
                }                
            }
        }
        
        
        return result;
    }
    
}
