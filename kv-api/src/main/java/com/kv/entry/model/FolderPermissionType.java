package com.kv.entry.model;

import com.kv.entry.asp.i18n.I18nKeyProv;

/**
 *
 * @author k-iderr
 */
public enum FolderPermissionType implements I18nKeyProv{
    OWNER,READ,WRITE;    
    
    @Override
    public String getKey() {
        return "enum.FolderPermissionType.".concat(name());
    }
}
