/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.entry.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author k-iderr
 */
public class BatchIdObjDto <I extends Serializable,E>{
    
    private List<I> ids;
    private E obj;

    public List<I> getIds() {
        return ids;
    }

    public void setIds(List<I> ids) {
        this.ids = ids;
    }

    public E getObj() {
        return obj;
    }

    public void setObj(E obj) {
        this.obj = obj;
    }
    
}
