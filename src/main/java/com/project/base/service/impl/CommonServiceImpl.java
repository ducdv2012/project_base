package com.project.base.service.impl;

import com.project.base.service.interfaces.CommonService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Service
public class CommonServiceImpl implements CommonService {

    public Map<String, String> mapValidationService(BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return errorMap;
        }

        return null;
    }
}
