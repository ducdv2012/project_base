package com.project.base.response;

import com.project.base.model.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponse {
    private Long id;
    private String username;
    private String email;
    private Set<Roles> roles = new HashSet<>();
}
