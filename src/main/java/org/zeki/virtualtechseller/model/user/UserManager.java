package org.zeki.virtualtechseller.model.user;

import javafx.collections.ObservableList;
import org.zeki.virtualtechseller.dto.user.ModifyUserDto;

public interface UserManager {

    void changeUserAccess(ObservableList<User> users, User selectedUser, boolean status);

    void updateUser(User selectedUser, ModifyUserDto userDto);
}
