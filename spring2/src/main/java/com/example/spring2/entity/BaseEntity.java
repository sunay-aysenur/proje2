package com.example.spring2.entity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Data //getter, setter, equals(), hashcode(), toString, @RequiredArgsConstructor otomatik olarak oluşturur.
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @CreationTimestamp
    @Column(updatable = false) // Oluşturulma tarihi update işlemlerinden etkilenmesin diye yazıldı. (Oluşturulma tarihi değiştirilemez)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
