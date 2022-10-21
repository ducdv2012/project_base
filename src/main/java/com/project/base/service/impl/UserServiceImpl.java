package com.project.base.service.impl;

import com.project.base.exception.AuthExceptionHandle;
import com.project.base.model.Users;
import com.project.base.repository.UserRepository;
import com.project.base.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    @SneakyThrows
    @Override
    public Users loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByUsername(username);
        if (users == null)
            throw new AuthExceptionHandle("Username not found");
        return users;
    }
}
