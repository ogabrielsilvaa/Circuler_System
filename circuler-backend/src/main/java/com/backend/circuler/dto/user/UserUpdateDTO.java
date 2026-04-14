package com.backend.circuler.dto.user;

import com.backend.circuler.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public class UserUpdateDTO {
    private String name;
    private String email;
    private String password;

    @Schema(
            type = "integer",
            allowableValues = {"0", "1", "2"},
            description = "Status do usuário: 1 - ATIVO, 2 - INATIVO, 0 - APAGADO",
            example = "1"
    )
    private UserStatus status;

    public UserUpdateDTO() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
}
