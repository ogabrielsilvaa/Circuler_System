package com.backend.circuler.dto.user;

import com.backend.circuler.enums.UserStatus;

import java.util.Set;

public class UserResponseDTO {
    private Integer id;
    private String name;
    private String email;
    private UserStatus status;
    private Set<String> roles;

    public UserResponseDTO() {}

    public UserResponseDTO(Integer id, String name, String email, UserStatus status, Set<String> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.status = status;
        this.roles = roles;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
}