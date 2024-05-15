/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.view.notification;

import com.kv.app.view.notification.dto.NotifDto;
import com.kv.app.KvAppUtils;
import com.kv.app.exc.BaseExcMapper;
import com.kv.app.view.MainView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.ListDataProvider;
import java.util.List;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import java.util.ArrayList;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import com.kv.app.view.notification.client.NotifResClient;
import com.kv.app.view.notification.dto.NotifUserDto;
import com.vaadin.flow.component.AttachEvent;
import jakarta.inject.Named;

/**
 *
 * @author k-iderr
 */
@Route(value = NotificationsView.PATH,layout = MainView.class)
@RolesAllowed("user")
public class NotificationsView extends VerticalLayout{
    
    public static final String PATH = "notifications";
    
    @Inject
    @Named("subjLang")
    private String userLang;    
    
    @RestClient
    private NotifResClient notifResClient;
    
    boolean isFolderNotifEnable;
    private Button folderNotifToggle;
    
    private final VirtualList<NotifDto> notifItems;
    private final ListDataProvider<NotifDto> notifItemsProvider;
    
    private List<NotifDto> notifsData;
    
    @PostConstruct
    public void postCon(){
      VaadinSession.getCurrent().setErrorHandler(new BaseExcMapper());
      refreshNotifData();
    }
    
    public NotificationsView(){
        setWidthFull();
        setHeightFull();
        notifsData = new ArrayList<>();
           
        notifItems = new VirtualList<>();
        notifItemsProvider = new ListDataProvider<>(notifsData);
        notifItems.setDataProvider(notifItemsProvider);
        notifItems.setRenderer(new ComponentRenderer<>(this::getFolderItemView));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        isFolderNotifEnable = notifResClient.getNotifUser(userLang).getEnableFoldersNotifs();
        folderNotifToggle = new Button(KvAppUtils.getResString("view.notifications.folder-notifs-"+isFolderNotifEnable),
                e->{
                    
                    NotifUserDto conf = new NotifUserDto();
                    conf.setEnableFoldersNotifs(!isFolderNotifEnable);
                    notifResClient.updateNotifUser(conf, userLang);
                    isFolderNotifEnable = !isFolderNotifEnable;
                    folderNotifToggle.setText(KvAppUtils.getResString("view.notifications.folder-notifs-"+isFolderNotifEnable));
                    
                });
        add(folderNotifToggle,notifItems);
    }
    
    
    
    private Component getFolderItemView(NotifDto notifDto){
        Span notifDesc = new Span(notifDto.getDisplayDescription());
        
        Button deleteButton = new Button(VaadinIcon.TRASH.create(),e->{
                notifResClient.deleteNotifById(notifDto.getId(),userLang);
                refreshNotifData();
        });

        deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        deleteButton.getElement().setAttribute("aria-label", "Add item");        
        deleteButton.setWidth("5%");
        
        
        HorizontalLayout itemViewParent = new HorizontalLayout(VaadinIcon.BELL.create(),
        notifDesc);
        
        if(notifDto.getNotifType() == NotifDto.NotifType.INVITE){
            if(notifDto.getNotifStatus() == NotifDto.NotifStatus.CREATED){

                Button confirmButton = new Button(VaadinIcon.CHECK_CIRCLE.create(),e->{
                    notifResClient.confirmNotification(notifDto.getId(),userLang);
                    refreshNotifData();
                });
                
                itemViewParent.add(confirmButton);
            }
        }
        
        itemViewParent.add(deleteButton);
        
        itemViewParent.setAlignItems(Alignment.CENTER);
        itemViewParent.setJustifyContentMode(JustifyContentMode.AROUND); 
        
        return itemViewParent;
    }  
    
    private void refreshNotifData(){
        List<NotifDto> newList = notifResClient.getAllNotifsByUser(null, null,userLang);
        notifsData.clear();
        notifsData.addAll(newList);
        notifItemsProvider.refreshAll();
    }
}
