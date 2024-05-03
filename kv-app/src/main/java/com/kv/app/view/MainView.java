package com.kv.app.view;

import com.kv.app.KvAppUtils;
import com.kv.app.view.entry.FoldersView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
@RolesAllowed("user")
public class MainView extends AppLayout {
    
    @ConfigProperty(name = "quarkus.oidc.auth-server-url")
    String authUrl;
    

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("KeyVal");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");
        
        Button logoutButton = new Button(VaadinIcon.ESC.create(), e->{
        UI.getCurrent().getPage().setLocation("/logout");
        });
        
        HorizontalLayout titleAndlogOutLayout = new HorizontalLayout(title,logoutButton);
        titleAndlogOutLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.AROUND);

        SideNav nav = getSideNav();

        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(scroller);
        addToNavbar(toggle, titleAndlogOutLayout);
    }
    
    
    
    
    private SideNav getSideNav(){
        SideNav nav = new SideNav();
        SideNavItem foldersLink = new SideNavItem(KvAppUtils.getResString("menu.folders"),
                FoldersView.class, VaadinIcon.FOLDER.create());
        
        SideNavItem settingsLink = new SideNavItem(KvAppUtils.getResString("menu.settings"),
                authUrl+"/account", VaadinIcon.EDIT.create());
        
        nav.addItem(foldersLink,settingsLink);        
        
        return nav;
    }
}
