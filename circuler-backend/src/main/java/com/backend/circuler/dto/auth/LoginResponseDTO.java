package com.backend.circuler.dto.auth;

import java.util.List;

public class LoginResponseDTO {

    private Integer id;
    private String token;
    private String email;
    private List<String> roles;

    public LoginResponseDTO() {}

    public LoginResponseDTO(Integer id, String token, String email, List<String> roles) {
        this.id = id;
        this.token = token;
        this.email = email;
        this.roles = roles;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}