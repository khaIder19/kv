package com.kv.entry.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 *
 * @author k-iderr
 */
@MappedSuperclass
public class BaseEntity {
    
    @Column(name = "created_at")
    private Long createdAt;
    
    @Column(name = "updated_at")    
    private Long updatedAt;
    
}
