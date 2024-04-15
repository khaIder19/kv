/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.view.entry;

import com.kv.app.exc.BaseExcMapper;
import com.kv.app.KvAppUtils;
import com.kv.app.view.entry.client.EntryResClient;
import com.kv.app.view.entry.data.EntryDto;
import com.kv.app.view.entry.data.FolderDto;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import io.quarkus.oidc.IdToken;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import com.kv.app.view.entry.client.EntryResClient;
import com.kv.app.view.entry.data.FolderPermissionDto;
import com.kv.app.view.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;

/**
 *
 * @author k-iderr
 */

@Route(value = "/folder",layout = MainView.class)
@RolesAllowed("user")
public class FolderItemView extends VerticalLayout implements HasUrlParameter<Long>{
    
    @Inject
    @IdToken
    private JsonWebToken token;
    
    @RestClient
    private EntryResClient entryResClient;    
    
    private final VirtualList<EntryDto> entryItems;
    private final ListDataProvider<EntryDto> entryItemsProvider;
    
    private TextField folderNameTxtField;
    
    private List<EntryDto> entriesData;
    private UUID currentUserId;
    private FolderDto folderDto;
    
    @PostConstruct
    public void postCon(){
      currentUserId = UUID.fromString(token.getSubject());
      VaadinSession.getCurrent().setErrorHandler(new BaseExcMapper());  
    }
    
    
    /**
     * FolderNameEditText             InsertEntry/Invite/Permissions
     * EntryList
     */
    
    
    public FolderItemView(){
        setWidthFull();
        setHeightFull();
        entriesData = new ArrayList<>();                     
        
        Button insertEntryButton = new Button("Insert value", VaadinIcon.PLUS.create(),
                e->{
                EntryDto dto = new EntryDto();
                    getEntryEditDialog(null).open();
                });        
        insertEntryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        Button viewPermissButton = new Button("Permissions", VaadinIcon.USER.create(),
                e->{
                    Dialog d = getFolderPermissionsDialog();
                    d.open();
                });        
        viewPermissButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);        
        
        Button inviteButton = new Button("Invite", VaadinIcon.SHARE.create(),
                e->{
                    getSendInviteDialog().open();
                });        
        inviteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);         
        
        folderNameTxtField = new TextField();
        folderNameTxtField.setLabel("Folder name");
        
        Button editFolderButton = new Button("Edit folder", VaadinIcon.CHECK.create(),
                e->{   
                folderDto.setName(folderNameTxtField.getValue());
                FolderDto updateDto = new FolderDto();
                updateDto.setName(folderDto.getName());
                entryResClient.upadeFolderById(folderDto.getId(),updateDto);
                
                });        
        editFolderButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);         
        
        
        HorizontalLayout headerLayout = new HorizontalLayout(folderNameTxtField,editFolderButton,
                insertEntryButton,viewPermissButton,inviteButton);
        
        
        
        entryItems = new VirtualList<>();
        entryItemsProvider = new ListDataProvider<>(entriesData);
        entryItems.setDataProvider(entryItemsProvider);
        entryItems.setRenderer(new ComponentRenderer<>(this::getEntryItemView));
        
        add(headerLayout,entryItems);
    }
    
    private Component getEntryItemView(EntryDto entryDto){
        TextField entryKeyField = new TextField("Key");
        entryKeyField.setValue(entryDto.getKey());
        entryKeyField.setEnabled(false);
        
        TextField entryValField = new TextField("Value");
        entryValField.setValue(entryDto.getValue().toString() != null ? entryDto.getValue().toString() : "NO VALUE");
        entryValField.setEnabled(false);        

        
        Button deleteButton = new Button(VaadinIcon.TRASH.create(),e->{
                entryResClient.deleteEntryById(folderDto.getId(), entryDto.getId());
                refreshEntryData();
        });

        deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        deleteButton.getElement().setAttribute("aria-label", "Add item");
        
        HorizontalLayout itemViewParent = new HorizontalLayout();
        itemViewParent.addClickListener(t->{
            getEntryEditDialog(entryDto).open();
        });

        itemViewParent.setAlignItems(FlexComponent.Alignment.CENTER);
        itemViewParent.add(VaadinIcon.KEY.create());
        itemViewParent.add(entryKeyField,entryValField,deleteButton);
        return itemViewParent;
    }
    
    
    private void refreshEntryData(){
        List<EntryDto> newList = entryResClient.getAllEntryByFolderId(folderDto.getId(), null, null,false);
        entriesData.clear();
        entriesData.addAll(newList);
        entryItemsProvider.refreshAll();
    }
    
    @Override
    public void setParameter(BeforeEvent be, Long t) {
        folderDto = entryResClient.getFolderById(t);
        folderNameTxtField.setValue(folderDto.getName());
        refreshEntryData();
    }
    
    private Dialog getFolderPermissionsDialog(){
        VirtualList<FolderPermissionDto> permissionsList = new VirtualList<>();        
        ListDataProvider<FolderPermissionDto> permissionsListDp = 
                new ListDataProvider<>(folderDto.getPermissions());
        
        permissionsList.setRenderer(new ComponentRenderer<Component,FolderPermissionDto>(dto->{
            Button deleteButton = new Button(VaadinIcon.CLOSE.create(),e->{
            entryResClient.deleteFolderPermissionById(folderDto.getId(),dto.getId());
            folderDto = entryResClient.getFolderById(folderDto.getId());
            permissionsListDp.refreshAll();
            });
            Span userNameSpan = new Span(dto.getUser().getName());
            Span permissinTypeSpan = new Span(dto.getPermissionType());            
            HorizontalLayout listItemLayout = new HorizontalLayout(userNameSpan,
                    permissinTypeSpan,deleteButton);
            return listItemLayout;
        }));
        
        permissionsList.setDataProvider(permissionsListDp);
        return KvAppUtils.getBasicDialog("Folder permissions",permissionsList);
    }    
    
    private Dialog getEntryEditDialog(EntryDto entryDto){
        TextField entryKeyTxField = new TextField();
        entryKeyTxField.setLabel("Entry key");
        TextField entryValueTxField = new TextField();
        entryValueTxField.setLabel("Entry value");
        
        if(entryDto != null){
            entryKeyTxField.setValue(entryDto.getKey());
            if(entryDto.getValue() != null)entryValueTxField.setValue(entryDto.getValue());
        }
        
        HorizontalLayout entryEditLayout = new HorizontalLayout(entryKeyTxField,entryValueTxField);
        Dialog dialog = KvAppUtils.getBasicDialog("Entry edit", entryEditLayout,
                e->{
                EntryDto editResultEntry = new EntryDto();
                editResultEntry.setKey(entryKeyTxField.getValue());
                editResultEntry.setValue(entryValueTxField.getValue());
                if(entryDto != null){
                    entryResClient.updateEntry(folderDto.getId(), entryDto.getId(), editResultEntry);
                }else{
                    entryResClient.createEntry(folderDto.getId(), editResultEntry);                    
                }
                refreshEntryData();
                });
             
        return dialog;  
    }
    
    
    
    private Dialog getSendInviteDialog(){
        TextField sendToEmailField = new TextField();
        sendToEmailField.setLabel("User email");
        
        Select<String> select = new Select<>();
        select.setLabel("Permission");
        select.setItems("READ","WRITE");
        select.setValue("READ");
        
        HorizontalLayout sendInviteLayout = new HorizontalLayout(sendToEmailField,select);
        Dialog dialog = KvAppUtils.getBasicDialog("Entry edit", sendInviteLayout,
                e->{
                         Notification.show("Notification sent");                
                });
             
        return dialog;   
    }  
}
