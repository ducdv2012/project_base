package com.project.base.service.interfaces;

import com.project.base.api.response.ApiResponse;
import com.project.base.request.UserRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    ApiResponse getList(String search, Long pageNo, Long pageSize);

    ApiResponse getDetail(Long id);

    ApiResponse create(UserRequest request);

    ApiResponse update(UserRequest request);
}
