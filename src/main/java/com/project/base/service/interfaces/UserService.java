package com.project.base.service.interfaces;

import com.project.base.model.Users;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    Users loadUserByUsername(String username);
}
