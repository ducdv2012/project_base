package com.project.base.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private Long id;
    @NotBlank
    @UniqueElements
    private String username;
    @NotBlank
    private String email;
    private String password;
    private List<Long> roleIds = new ArrayList<>();
}
