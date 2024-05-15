/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.notif.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kv.notif.asp.i18n.I18nSerializer;
import com.kv.notif.asp.i18n.I18nValueWrap;
import static com.kv.notif.model.NotifType.ENTRY_CREATED;
import static com.kv.notif.model.NotifType.ENTRY_DELETED;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author k-iderr
 */
@Entity
@Table(name = "notif")
@SequenceGenerator(name = "seq_gen_id_notif",sequenceName = "seq_gen_id_notif",initialValue = 1,allocationSize = 1)
public class Notif extends BaseEntity{
    
    public static final String ENTRY_KEY = "entry_key";
    public static final String ENTRY_FOLDER_NAME = "entry_folder_name";
    
    public static final String INVITE_FOLDER_ID = "invite_folder_id";
    public static final String INVITE_FOLDER_NAME = "invite_folder_name";
    public static final String INVITE_FOLDER_FROM_USER_NAME = "invite_folder_from_user_name";    
    public static final String INVITE_FOLDER_FROM_USER = "invite_folder_from_user";
    public static final String INVITE_FOLDER_TO_USER = "invite_folder_to_user";    
    public static final String INVITE_FOLDER_PERMISSION_TYPE = "invite_folder_permission_type";    
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq_gen_id_notif")
    private Long id;
    
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id",updatable = false)
    private NotifUser user;
    
    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "notif_type",nullable = false)
    @JsonSerialize(using = I18nSerializer.class)
    private NotifType notifType;
    
    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "notif_status",nullable = false)
    @JsonSerialize(using = I18nSerializer.class)
    private NotifStatus notifStatus;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="notif_props", joinColumns=@JoinColumn(name="notif_id"))
    @MapKeyColumn(name="prop_name")
    @Column(name="prop_value") 
    private Map<String,String> props; 

    protected Notif(){
        
    }
    
    public Notif(NotifUser user, NotifType notifType, NotifStatus notifStatus) {
        this.user = user;
        this.notifType = notifType;
        this.notifStatus = notifStatus;
        this.props = new HashMap<>(8);
    }

    @JsonProperty(value = "displayDescription")
    @JsonSerialize(using = I18nSerializer.class)
    public I18nValueWrap getDisplayDescription() {
        return new I18nValueWrap("notif.desc."+notifType.name(),getFormaPropsForNotifType(notifType));
    }
    
    

    
    public Long getId() {
        return id;
    }
    
    public NotifUser getUser() {
        return user;
    }
    
    public NotifType getNotifType() {
        return notifType;
    }

    public NotifStatus getNotifStatus() {
        return notifStatus;
    }

    public void setNotifStatus(NotifStatus notifStatus) {
        this.notifStatus = notifStatus;
    }
    
    public Map<String,String> getProps(){   
        return props;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }   
      
    public String[] getFormaPropsForNotifType(NotifType type){
        List<String> propsList = new ArrayList<>();
        switch (type) {
            case ENTRY_DELETED:
            case ENTRY_UPDATED:
            case ENTRY_CREATED:
                propsList.add(getProps().get(ENTRY_KEY));
                propsList.add(getProps().get(ENTRY_FOLDER_NAME));                
                break;
            case INVITE:
                propsList.add(getProps().get(INVITE_FOLDER_FROM_USER_NAME));
                propsList.add(getProps().get(INVITE_FOLDER_NAME));                 
                break;
        }
        String[] props = new String[propsList.size()];
        return propsList.toArray(props);
    }
}
