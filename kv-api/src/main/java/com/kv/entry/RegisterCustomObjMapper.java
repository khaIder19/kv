/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kv.entry.dto.DtoViews;
import io.quarkus.jackson.ObjectMapperCustomizer;
import jakarta.inject.Singleton;

/**
 *
 * @author k-iderr
 */

@Singleton
public class RegisterCustomObjMapper implements ObjectMapperCustomizer{

    @Override
    public void customize(ObjectMapper objectMapper) {
        objectMapper.setConfig(objectMapper.getSerializationConfig()
                .withView(DtoViews.Rest.class));
    }
    
}
