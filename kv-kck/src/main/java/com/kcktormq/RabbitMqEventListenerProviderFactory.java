/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kcktormq;

import com.kcktormq.RabbitMqEventListenerProvider;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

/**
 *
 * @author k-iderr
 */
public class RabbitMqEventListenerProviderFactory implements EventListenerProviderFactory{


    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Channel channel;    
    private String exchangeName;
    
    @Override
    public EventListenerProvider create(KeycloakSession ks) {
        checkConnectionAndChannel();
        return new RabbitMqEventListenerProvider(channel,ks,exchangeName);
    }
    
    
    private synchronized void checkConnectionAndChannel() {
        try {
            if (connection == null || !connection.isOpen()) {
                this.connection = connectionFactory.newConnection();
            }
            if (channel == null || !channel.isOpen()) {
                channel = connection.createChannel();
            }
            if(channel != null && channel.isOpen()){
                channel.exchangeDeclare(exchangeName,BuiltinExchangeType.FANOUT,true);            
            }
        }
        catch (IOException | TimeoutException e) {
            e.printStackTrace();
            System.out.println("Failed to open connection kck-to-rmq");
        }
            System.out.println("OK connection kck-to-rmq");        
    }    

    @Override
    public void init(Config.Scope scope) {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername(getEnvOrDefaultParam("KK_TO_RMQ_USER","guest"));
        connectionFactory.setPassword(getEnvOrDefaultParam("KK_TO_RMQ_PASS","guest"));
        connectionFactory.setHost(getEnvOrDefaultParam("KK_TO_RMQ_HOST","rabbitmq"));
        connectionFactory.setPort(Integer.valueOf(getEnvOrDefaultParam("KK_TO_RMQ_PORT","5672")));
        connectionFactory.setAutomaticRecoveryEnabled(true);   
        exchangeName = getEnvOrDefaultParam("KK_TO_RMQ_EXC_NAME", "kck-events");
    }
    
    private String getEnvOrDefaultParam(String param,String defaultVal){
        String envVal = System.getenv(param);
        if(envVal == null){
            envVal = defaultVal;
        }
        return envVal;
    }

    @Override
    public void postInit(KeycloakSessionFactory ksf) {
        
    }

    @Override
    public void close() {
        try {
            channel.close();
            connection.close();
        }
        catch (IOException | TimeoutException e) {
            System.out.println("Failed to close connection kck-to-rmq");
        }        
    }

    @Override
    public String getId() {
        return "keycloak-to-rabbitmq";
    }
    
}
