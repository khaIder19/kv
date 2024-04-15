/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.view.entry;

import com.kv.app.exc.BaseExcMapper;
import com.kv.app.view.entry.data.FolderDto;
import com.kv.app.view.entry.data.FolderPermissionDto;
import com.kv.app.view.entry.data.FolderPermissionType;
import com.kv.app.view.MainView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.ListDataProvider;
import java.util.List;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import io.quarkus.oidc.IdToken;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.UUID;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import com.kv.app.view.entry.client.EntryResClient;

/**
 *
 * @author k-iderr
 */
@Route(value = FoldersView.PATH,layout = MainView.class)
@RolesAllowed("user")
public class FoldersView extends VerticalLayout{
    
    public static final String PATH = "folders";
    
    @Inject
    @IdToken
    private JsonWebToken token;
    
    @RestClient
    private EntryResClient entryResClient;
    
    private final VirtualList<FolderDto> folderItems;
    private final ListDataProvider<FolderDto> folderItemsProvider;
    
    private List<FolderDto> foldersData;
    private UUID currentUserId;
    private FolderPermissionType fptFilter;
    
    @PostConstruct
    public void postCon(){
      currentUserId = UUID.fromString(token.getSubject());
      VaadinSession.getCurrent().setErrorHandler(new BaseExcMapper());
      refreshFolderData();
    }
    
    public FoldersView(){
        setWidthFull();
        setHeightFull();
        foldersData = new ArrayList<>();
        
        Tab allTab = new Tab("All");
        allTab.setId("allTab");
        Tab mineTab = new Tab("Mine");
        mineTab.setId("mineTab");
        Tabs tabs = new Tabs(allTab, mineTab);
        tabs.addSelectedChangeListener(new ComponentEventListener<Tabs.SelectedChangeEvent>(){
            @Override
            public void onComponentEvent(Tabs.SelectedChangeEvent t) {
                if(t.getSelectedTab().getLabel().equals("mineTab")){
                    fptFilter = FolderPermissionType.OWNER;
                }else{
                    fptFilter = null;
                }
                refreshFolderData();
            }      
        });
        
       
        TextField folderNameField = new TextField();
        
        Button insertFolderButton = new Button("Add folder", VaadinIcon.PLUS.create(),
                e->{
                FolderDto dto = new FolderDto();
                dto.setName(folderNameField.getValue());
                entryResClient.createFolder(dto);
                refreshFolderData();
                });
        
        insertFolderButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout addNewFolderLayout = new HorizontalLayout(folderNameField,insertFolderButton);
        
        
        
        folderItems = new VirtualList<>();
        folderItemsProvider = new ListDataProvider<>(foldersData);
        folderItems.setDataProvider(folderItemsProvider);
        folderItems.setRenderer(new ComponentRenderer<>(this::getFolderItemView));
        
        add(addNewFolderLayout,tabs,folderItems);
    }
    
    private Component getFolderItemView(FolderDto folderDto){
        Span folderNameSpan = new Span(folderDto.getName());
        folderNameSpan.addClassName("folders-view-span-1");
        Span ownerNameSpan = new Span(folderDto.getOwner().getName());
        Span permissionSpan = new Span(getCurrentUserPermissionOfFolder(folderDto));
        
        Button deleteButton = new Button(VaadinIcon.PLUS.create(),e->{
                entryResClient.deleteFolderById(folderDto.getId());
                refreshFolderData();
        });
        deleteButton.addClassName("folders-view-button-1");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        deleteButton.getElement().setAttribute("aria-label", "Add item");
        
        HorizontalLayout itemViewParent = new HorizontalLayout();
        itemViewParent.addClickListener(t->{
                itemViewParent.getUI().ifPresent(ui -> ui.navigate(
                FolderItemView.class, folderDto.getId()));
        });
        itemViewParent.addClassName("folders-view-horizontal-layout-1");
        itemViewParent.setJustifyContentMode(JustifyContentMode.AROUND);
        itemViewParent.setAlignItems(Alignment.CENTER);
        itemViewParent.add(VaadinIcon.FOLDER.create());
        itemViewParent.add(folderNameSpan,ownerNameSpan,
                permissionSpan,deleteButton);
        return itemViewParent;
    }
    
    private String getCurrentUserPermissionOfFolder(FolderDto folderDto){
        String fp = folderDto.getPermissions()
                .stream().filter(p->p.getUser().getId().equals(currentUserId))
                .map(FolderPermissionDto::getPermissionType)
                .findFirst().get();
        return fp;
    }
    
    private void refreshFolderData(){
        List<FolderDto> newList = entryResClient.getAllFoldersByUser(fptFilter != null ? fptFilter.name() : null,
                null, null);
        foldersData.clear();
        foldersData.addAll(newList);
        folderItemsProvider.refreshAll();
    }
}
