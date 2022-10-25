package com.project.base.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditTable implements Serializable {
    @JsonIgnore
    @Column(name = "created_date", updatable = false, nullable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @JsonIgnore
    @Column(name = "updated_date")
    @LastModifiedDate
    private LocalDateTime updatedDate;

    @JsonIgnore
    @Column(name = "created_by", updatable = false, nullable = false)
    @CreatedBy
    private Long createdBy = 0L;

    @JsonIgnore
    @Column(name = "modified_by")
    @LastModifiedBy
    private Long modifiedBy = 0L;
}
