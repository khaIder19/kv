package com.kv.app.view;

import com.kv.app.view.entry.FoldersView;
import com.kv.app.view.entry.client.EntryResClient;
import com.kv.app.view.entry.data.FolderDto;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.quarkus.logging.Log;
import io.quarkus.oidc.IdToken;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
@RolesAllowed("user")
public class MainView extends AppLayout {
    
    public MainView() {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("MyApp");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        SideNav nav = getSideNav();

        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(scroller);
        addToNavbar(toggle, title);
        
    }
    
    
    private SideNav getSideNav(){
        SideNav nav = new SideNav();
        SideNavItem foldersLink = new SideNavItem("Folders",FoldersView.class, VaadinIcon.FOLDER.create());
        SideNavItem settingsLink = new SideNavItem("Settings",SettingsView.class, VaadinIcon.EDIT.create());
        nav.addItem(foldersLink,settingsLink);
        return nav;
    }
}
