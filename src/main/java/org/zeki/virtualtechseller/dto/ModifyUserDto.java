package org.zeki.virtualtechseller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zeki.virtualtechseller.model.user.Role;

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
    protected Role userRole;

}
