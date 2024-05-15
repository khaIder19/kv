package com.kv.notif.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author k-iderr
 */
@MappedSuperclass
public class BaseEntity {
    
    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "created_at")    
    private Instant createdAt;
    
    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = "updated_at")    
    private Instant updatedAt;
    
}
