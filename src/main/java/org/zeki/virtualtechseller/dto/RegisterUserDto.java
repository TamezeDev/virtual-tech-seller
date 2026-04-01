package org.zeki.virtualtechseller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zeki.virtualtechseller.model.user.UserRole;

@Getter
@Setter
@NoArgsConstructor
public class RegisterUserDto {

    protected String name;
    protected String lastName;
    protected String email;
    protected String phone;
    protected String password;
    protected UserRole userRole;

}
