package com.backend.circuler.dto.user;

import com.backend.circuler.enums.UserStatus;

import java.util.Set;

public class UserResponseDTO {
    private Integer id;
    private String name;
    private String email;
    private String cpf;
    private UserStatus status;
    private Set<String> roles;

    public UserResponseDTO() {}

    public UserResponseDTO(Integer id, String name, String email, String cpf, UserStatus status, Set<String> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.status = status;
        this.roles = roles;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
}