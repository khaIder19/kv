/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.notif.asp.i18n;

/**
 *
 * @author k-iderr
 */
public class I18nValueWrap implements I18nKeyProv{

    private String key;
    private String[] params;

    public I18nValueWrap(String key, String[] params) {
        this.key = key;
        this.params = params;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String[] getParams() {
        return params;
    }
    
}
