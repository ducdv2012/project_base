package com.project.base.service.interfaces;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Map;

@Service
public interface CommonService {
    Map<String, String> mapValidationService(BindingResult result);
}
