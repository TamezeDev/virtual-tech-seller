package org.zeki.virtualtechseller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zeki.virtualtechseller.model.user.UserRole;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyUserDto {

    private int idUser;
    protected String name;
    protected String lastName;
    protected String email;
    protected String phone;
    protected String password;
    protected UserRole userRole;

}
