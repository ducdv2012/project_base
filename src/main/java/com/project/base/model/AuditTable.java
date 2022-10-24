package com.project.base.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.base.util.converter.ZoneDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditTable implements Serializable {
    @JsonIgnore
    @Column(name = "created_date", updatable = false, nullable = false, columnDefinition = "0")
    @CreatedDate
    @Convert(converter = ZoneDateTimeConverter.class)
    private LocalDateTime createdDate;

    @JsonIgnore
    @Column(name = "updated_date")
    @LastModifiedDate
    @Convert(converter = ZoneDateTimeConverter.class)
    private LocalDateTime updatedDate;

    @JsonIgnore
    @Column(name = "created_by", updatable = false, nullable = false, columnDefinition = "0")
    @CreatedBy
    private Long createdBy;

    @JsonIgnore
    @Column(name = "modified_by")
    @LastModifiedBy
    private Long modifiedBy;
}
