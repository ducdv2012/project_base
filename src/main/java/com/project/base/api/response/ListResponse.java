package com.project.base.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ListResponse {
    private Long totalElements;
    private Long pageNo;
    private Long pageSize;
    private List<?> data;
}
