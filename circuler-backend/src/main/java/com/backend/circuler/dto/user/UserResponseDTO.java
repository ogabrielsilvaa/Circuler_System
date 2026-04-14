package com.backend.circuler.dto.user;

import com.backend.circuler.enums.UserStatus;

public class UserResponseDTO {
    private Integer id;
    private String name;
    private String email;
    private UserStatus status;

    public UserResponseDTO() {}

    public UserResponseDTO(Integer id, String name, String email, UserStatus status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.status = status;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
}
