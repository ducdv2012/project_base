package com.project.base.api.controller;

import com.project.base.api.response.ApiResponse;
import com.project.base.request.UserRequest;
import com.project.base.service.interfaces.CommonService;
import com.project.base.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CommonService commonService;

    @GetMapping
    public ApiResponse getList(@RequestParam(name = "search", required = false) String search,
                               @RequestParam(name = "pageNo", defaultValue = "1") Long pageNo,
                               @RequestParam(name = "pageSize", defaultValue = "20") Long pageSize) {
        return userService.getList(search, pageNo, pageSize);
    }

    @GetMapping("/{id}")
    public ApiResponse getDetail(@PathVariable Long id) {
        return userService.getDetail(id);
    }

    @PostMapping
    public ApiResponse create(@RequestBody UserRequest request, BindingResult result) {
        Map<String, String> map = commonService.mapValidationService(result);
        if (map != null) {
            return new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Has some field is bad request", map);
        }
        return userService.create(request);
    }

    @PutMapping
    public ApiResponse update(@RequestBody UserRequest request, BindingResult result) {
        Map<String, String> map = commonService.mapValidationService(result);
        if (map != null) {
            return new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Has some field is bad request", map);
        }
        return userService.update(request);
    }
}
