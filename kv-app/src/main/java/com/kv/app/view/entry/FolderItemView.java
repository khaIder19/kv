/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.app.view.entry;

import com.kv.app.exc.BaseExcMapper;
import com.kv.app.KvAppUtils;
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
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import com.kv.app.view.entry.client.EntryResClient;
import com.kv.app.view.entry.data.FolderPermissionDto;
import com.kv.app.view.MainView;
import com.kv.app.view.entry.data.FolderPermissionType;
import com.kv.app.view.notification.dto.FolderInviteDto;
import com.kv.app.view.notification.client.NotifResClient;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import jakarta.inject.Named;
import java.util.UUID;

/**
 *
 * @author k-iderr
 */

@Route(value = "/folder",layout = MainView.class)
@RolesAllowed("user")
public class FolderItemView extends VerticalLayout implements HasUrlParameter<Long>{
    
    
    @RestClient
    private EntryResClient entryResClient;
    
    @RestClient
    private NotifResClient notifResClient;    

    @Inject
    @Named("subjLang")
    private String userLang;
    
    @Inject
    @Named("subjUUID")
    private UUID subjUuid;    
    
    private final VirtualList<EntryDto> entryItems;
    private final ListDataProvider<EntryDto> entryItemsProvider;
    
    private TextField folderNameTxtField;
    
    private List<EntryDto> entriesData;
    private List<FolderPermissionDto> permissionsData;
    private FolderDto folderDto;
    
    @PostConstruct
    public void postCon(){
      VaadinSession.getCurrent().setErrorHandler(new BaseExcMapper());  
    }
    
    
    
    public FolderItemView(){
        setWidthFull();
        setHeightFull();
        entriesData = new ArrayList<>();
        permissionsData = new ArrayList<>();
        
        Button insertEntryButton = new Button(KvAppUtils.getResString("view.folder.insert_entry"), VaadinIcon.PLUS.create(),
                e->{
                EntryDto dto = new EntryDto();
                    getEntryEditDialog(null).open();
                });        
        insertEntryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        Button viewPermissButton = new Button(KvAppUtils.getResString("view.folder.view_permissions"),
                VaadinIcon.USER.create(),
                e->{
                    Dialog d = getFolderPermissionsDialog();
                    d.open();
                });        
        viewPermissButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);        
        
        Button inviteButton = new Button(KvAppUtils.getResString("view.folder.invite_user"),
                VaadinIcon.SHARE.create(),
                e->{
                    getSendInviteDialog().open();
                });        
        inviteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);         
        
        folderNameTxtField = new TextField();
        folderNameTxtField.setLabel(KvAppUtils.getResString("dto.FolderDto.name"));
        
        Button editFolderButton = new Button(KvAppUtils.getResString("view.folder.update_folder"),
                VaadinIcon.CHECK.create(),
                e->{   
                folderDto.setName(folderNameTxtField.getValue());
                FolderDto updateDto = new FolderDto();
                updateDto.setName(folderDto.getName());
                entryResClient.upadeFolderById(folderDto.getId(),updateDto,userLang);
                
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
        TextField entryKeyField = new TextField(KvAppUtils.getResString("dto.EntryDto.key"));
        entryKeyField.setValue(entryDto.getKey());
        entryKeyField.setEnabled(false);
        entryKeyField.setWidth("50%");
        
        TextField entryValField = new TextField(KvAppUtils.getResString("dto.EntryDto.value"));
        entryValField.setValue(entryDto.getValue().toString() != null ? entryDto.getValue().toString() : "NO VALUE");
        entryValField.setEnabled(false);
        entryValField.setWidth("50%");        

        
        Button deleteButton = new Button(VaadinIcon.TRASH.create(),e->{
                entryResClient.deleteEntryById(folderDto.getId(), entryDto.getId(),userLang);
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
        List<EntryDto> newList = entryResClient.getAllEntryByFolderId(folderDto.getId(),
                null, null,false,userLang);
        entriesData.clear();
        entriesData.addAll(newList);
        permissionsData.clear();
        permissionsData.addAll(folderDto.getPermissions());
        entryItemsProvider.refreshAll();
    }
    
    @Override
    public void setParameter(BeforeEvent be, Long t) {
        folderDto = entryResClient.getFolderById(t,userLang);
        folderNameTxtField.setValue(folderDto.getName());
        refreshEntryData();
    }
    
    private Dialog getFolderPermissionsDialog(){
        VirtualList<FolderPermissionDto> permissionsList = new VirtualList<>();        
        ListDataProvider<FolderPermissionDto> permissionsListDp = 
                new ListDataProvider<>(permissionsData);
        
        permissionsList.setRenderer(new ComponentRenderer<Component,FolderPermissionDto>(dto->{
            Button deleteButton = new Button(VaadinIcon.CLOSE.create(),e->{
            entryResClient.deleteFolderPermissionById(folderDto.getId(),dto.getId(),userLang);
            folderDto = entryResClient.getFolderById(folderDto.getId(),userLang);
            permissionsData.clear();
            permissionsData.addAll(folderDto.getPermissions());
            permissionsListDp.refreshAll();
            });
            deleteButton.setWidth("10%");
            Span userNameSpan = new Span(dto.getUser().getName());
            userNameSpan.setWidth("45%");
            Span permissinTypeSpan = new Span(dto.getPermissionType());            
            permissinTypeSpan.setWidth("45%");
            HorizontalLayout listItemLayout = new HorizontalLayout(userNameSpan,
                    permissinTypeSpan,deleteButton);
            return listItemLayout;
        }));
        
        permissionsList.setDataProvider(permissionsListDp);
        return KvAppUtils.getBasicDialog(KvAppUtils.getResString("view.folder.view_permissions"),permissionsList);
    }    
    
    private Dialog getEntryEditDialog(EntryDto entryDto){
        TextField entryKeyTxField = new TextField();
        entryKeyTxField.setLabel(KvAppUtils.getResString("dto.EntryDto.key"));
        entryKeyTxField.setWidth("50%");
        TextField entryValueTxField = new TextField();
        entryValueTxField.setLabel(KvAppUtils.getResString("dto.EntryDto.value"));
        entryValueTxField.setWidth("50%");        
        
        if(entryDto != null){
            entryKeyTxField.setValue(entryDto.getKey());
            if(entryDto.getValue() != null)entryValueTxField.setValue(entryDto.getValue());
        }
        
        HorizontalLayout entryEditLayout = new HorizontalLayout(entryKeyTxField,entryValueTxField);
        Dialog dialog = KvAppUtils.getBasicDialog(KvAppUtils.getResString("view.entry.edit.title"),
                entryEditLayout,
                e->{
                EntryDto editResultEntry = new EntryDto();
                editResultEntry.setKey(entryKeyTxField.getValue());
                editResultEntry.setValue(entryValueTxField.getValue());
                if(entryDto != null){
                    entryResClient.updateEntry(folderDto.getId(), entryDto.getId(), editResultEntry,userLang);
                }else{
                    entryResClient.createEntry(folderDto.getId(), editResultEntry,userLang);                    
                }
                refreshEntryData();
                });
             
        return dialog;  
    }
    
    
    
    private Dialog getSendInviteDialog(){
        TextField sendToEmailField = new TextField();
        sendToEmailField.setLabel(KvAppUtils.getResString("dto.FolderInvite.email"));
        
        Select<String> select = new Select<>();
        select.setLabel(KvAppUtils.getResString("dto.FolderInvite.permission"));
        select.setItems(KvAppUtils.getResArray("enum.FolderPermissionType.partial"));
        
        HorizontalLayout sendInviteLayout = new HorizontalLayout(sendToEmailField,select);
        Dialog dialog = KvAppUtils.getBasicDialog(KvAppUtils.getResString("view.folder.invite_user"), sendInviteLayout,
                e->{
                    if(folderDto.getOwner().getId().equals(subjUuid)){
                        int indexOfSelectFp = select.getItemPosition(select.getValue());
                        FolderPermissionType fpt = FolderPermissionType.values()[indexOfSelectFp+1];
                                
                        FolderInviteDto fInvDto = new FolderInviteDto(folderDto.getId(),
                                folderDto.getName(), sendToEmailField.getValue(),
                                fpt.name());
                        
                        notifResClient.sendFolderInvite(fInvDto);
                    }                
                });
             
        return dialog;   
    }  
}
