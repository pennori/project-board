package com.board.api.global.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@MappedSuperclass
@ToString
@Getter
@NoArgsConstructor
public class DateAndAuthor {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    @Setter
    private Long updatedBy;

    public DateAndAuthor(Long createdBy) {
        this.createdBy = createdBy;
        this.updatedBy = this.createdBy;
    }

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
