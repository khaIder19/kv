/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.auth.ViewAccessChecker;
import io.quarkus.oidc.IdToken;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Route
@PermitAll
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    public static final String PATH = "login";

        
        
    public LoginView() {
        add("Hello :-)");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var session = VaadinRequest.getCurrent().getWrappedSession();
        //ViewAccessChecker saves the original route to session
        // restore that when we are returned form OIDC server
        Object origView = session.getAttribute(ViewAccessChecker.SESSION_STORED_REDIRECT);
        if(origView != null) {
            event.forwardTo(origView.toString());
        } else {
            // This should never happen :-)
            // But happens if you manually enter login while already
            // logged in.
            event.forwardTo(MainView.class);
        }
    }
}
