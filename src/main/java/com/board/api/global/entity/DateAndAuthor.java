package com.board.api.global.entity;

import com.board.api.global.constants.Author;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private Long updatedBy;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        this.createdBy = Author.SYSTEM_ID;
        this.updatedBy = this.createdBy;
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = Author.SYSTEM_ID;
    }
}
