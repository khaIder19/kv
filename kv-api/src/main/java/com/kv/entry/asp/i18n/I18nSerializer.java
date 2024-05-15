/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.asp.i18n;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.kv.entry.NamedAnn;
import io.quarkus.logging.Log;
import jakarta.enterprise.inject.spi.CDI;
import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author k-iderr
 */
public class I18nSerializer extends StdSerializer{

    
    public I18nSerializer(){
        super(Object.class);
    }

    @Override
    public void serialize(Object t, JsonGenerator jg, SerializerProvider sp) throws IOException {
        String key = null;
        String value = null;
        if(t instanceof I18nKeyProv){
            key = ((I18nKeyProv)t).getKey();
        }else{
            key = t.toString();
        }
        
        try{
            value = getResBundle().getString(key);
            jg.writeString(value);
        }catch(MissingResourceException e){
            Log.errorf("Missing resource for key : %s", key);
            sp.defaultSerializeValue(t, jg);            
        }
        Log.debugf("The key %s us serialized with i18n value %s", key,value);
    }
    
    private ResourceBundle getResBundle(){
        return ResourceBundle.getBundle("I18NBundle", getLocale());   
    }
    
    private Locale getLocale(){
        Locale requestLocale = CDI.current().select(Locale.class,new NamedAnn("subjLocale")).get();
        return requestLocale;
    }
}
