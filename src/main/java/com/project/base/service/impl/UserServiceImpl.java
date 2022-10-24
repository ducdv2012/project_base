package com.project.base.service.impl;

import com.project.base.api.response.ApiResponse;
import com.project.base.api.response.ListResponse;
import com.project.base.exception.AuthExceptionHandle;
import com.project.base.model.Roles;
import com.project.base.model.Users;
import com.project.base.repository.CustomUserRepository;
import com.project.base.repository.RoleRepository;
import com.project.base.repository.UserRepository;
import com.project.base.request.UserRequest;
import com.project.base.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CustomUserRepository customUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse getList(String search, Long pageNo, Long pageSize) {
        try {
            ListResponse response = customUserRepository.findAllWithSearch(search, pageNo, pageSize);
            return new ApiResponse(HttpStatus.OK.value(), "Successfully", response);
        } catch (Exception e) {
            log.error("-------- Get list user error --------");
            log.error(e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Get list user failed");
        }
    }

    @Override
    public ApiResponse getDetail(Long id) {
        return null;
    }

    @Override
    public ApiResponse create(UserRequest request) {
        try {
            List<Roles> roles = roleRepository.findAllById(request.getRoleIds());

            Users users = new Users();
            users.setEmail(request.getEmail());
            users.setUsername(request.getUsername());
            users.setPassword(passwordEncoder.encode(request.getPassword()));
            users.setRoles(new HashSet<>(roles));
            userRepository.save(users);
            return new ApiResponse(HttpStatus.OK.value(), "Create user successfully", users);
        } catch (Exception e) {
            log.error("-------- Create user error --------");
            log.error(e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Create user failed");
        }
    }

    @Override
    public ApiResponse update(UserRequest request) {
        try {
            List<Roles> roles = roleRepository.findAllById(request.getRoleIds());

            Users users = findByUserId(request.getId());
            users.setEmail(request.getEmail());
            users.setUsername(request.getUsername());

            users.getRoles().clear();
            users.getRoles().addAll(roles);
            userRepository.save(users);
            return new ApiResponse(HttpStatus.OK.value(), "Update user successfully", users);
        } catch (Exception e) {
            log.error("-------- Update user error --------");
            log.error(e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Update user failed");
        }
    }

    private Users findByUserId(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
