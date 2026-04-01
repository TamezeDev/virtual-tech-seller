package org.zeki.virtualtechseller.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class User implements UserIdentity {

    protected int idUser;
    protected String name;
    protected String lastName;
    protected String email;
    protected String phone;
    protected String password;
    protected LocalDate createDate;
    protected Boolean emailActivate;

    public String checkAccess() {
        return Boolean.TRUE.equals(emailActivate) ? "Permitido" : "Bloqueado";
    }

    public String getFullName() {
        return String.format(name + " " + lastName);
    }

}
