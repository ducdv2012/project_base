package com.project.base.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.base.util.converter.LocalDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
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
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime createdDate = LocalDateTime.now();

    @JsonIgnore
    @Column(name = "updated_date")
    @LastModifiedDate
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime updatedDate = LocalDateTime.now();

    @JsonIgnore
    @Column(name = "created_by", updatable = false, nullable = false)
    @CreatedBy
    private Long createdBy;

    @JsonIgnore
    @Column(name = "modified_by")
    @LastModifiedBy
    private Long modifiedBy;
}
