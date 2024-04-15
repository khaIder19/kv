/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.view;

import com.kv.app.view.entry.client.EntryResClient;
import com.kv.app.view.entry.data.EntryUserDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import io.quarkus.oidc.IdToken;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import java.util.Random;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.token.TokenManager;

/**
 *
 * @author k-iderr
 */
@Route(value = "settings",layout = MainView.class)
@RolesAllowed("user")
public class SettingsView extends VerticalLayout{
    
    @Inject
    @IdToken
    private JsonWebToken token;
    
    
    
    public SettingsView(){
    }
    
    
    
}
