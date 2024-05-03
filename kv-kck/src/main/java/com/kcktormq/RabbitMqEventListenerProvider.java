/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kcktormq;

import com.rabbitmq.client.Channel;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import com.rabbitmq.client.AMQP.BasicProperties.Builder;
import com.rabbitmq.client.AMQP;
import org.keycloak.events.EventListenerTransaction;
import org.keycloak.util.JsonSerialization;

/**
 *
 * @author k-iderr
 */
public class RabbitMqEventListenerProvider implements EventListenerProvider{

    private Channel channel;
    private KeycloakSession session;
    private String exchangeName;
    
    private final EventListenerTransaction tx = new EventListenerTransaction((e,b)->{},this::publishEvent);
        
    public RabbitMqEventListenerProvider(Channel channel,KeycloakSession session,String exchangeName){
        this.channel = channel;
        this.session = session;
        this.exchangeName = exchangeName;
	this.session.getTransactionManager().enlistAfterCompletion(tx);        
    }
    
    @Override
    public void onEvent(Event event) {
	tx.addEvent(event);
    }
    
    private void publishEvent(Event event){
	EventDto msg = new EventDto();
	msg.setTime(event.getTime());
        msg.setRealm(event.getRealmId());
        msg.setClientId(event.getClientId());
        msg.setEventType(event.getType());
        msg.setUserId(event.getUserId());
        msg.setDetails(event.getDetails());
		
	Builder propsBuilder = new AMQP.BasicProperties.Builder()
				.appId("Keycloak")
				.contentType("application/json")
				.contentEncoding("UTF-8");

        String msgAsString = writeAsJson(msg, true);
	try {
	channel.basicPublish(exchangeName,"",propsBuilder.build(), msgAsString.getBytes());
	} catch (Exception ex) {
	}        
    }

    @Override
    public void onEvent(AdminEvent ae, boolean bln) {
        
    }

    @Override
    public void close() {
        
    }
    
    public static String writeAsJson(Object object, boolean isPretty) {
	try {
            if(isPretty) {
                return JsonSerialization.writeValueAsPrettyString(object);
            }
            return JsonSerialization.writeValueAsString(object);
            } catch (Exception e) {
		//log.error("Could not serialize to JSON", e);
        }
        return "unparseable";
    }    
    
}
