package org.zeki.virtualtechseller.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zeki.virtualtechseller.model.user.UserRole;

@Getter
@Setter
@NoArgsConstructor
public class RegisterUserDto {

    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private UserRole userRole;

}
