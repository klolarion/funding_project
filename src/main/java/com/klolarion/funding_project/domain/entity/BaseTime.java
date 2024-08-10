package com.klolarion.funding_project.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseTime {
    @CreatedDate
    @Column(updatable = false)
    private String createdDate;
    @LastModifiedDate
    private String lastModifiedDate;

//    @PrePersist
//    public void onPrePersist() {
//        this.createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS:SSS"));
//        this.lastModifiedDate  = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS:SSS"));
//    }
//
//    @PreUpdate
//    public void onPreUpdate() {
//        this.lastModifiedDate  = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS:SSS"));
//    }


}