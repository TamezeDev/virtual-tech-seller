package org.zeki.virtualtechseller.model.user;

import org.zeki.virtualtechseller.dto.ModifyUserDto;
import org.zeki.virtualtechseller.service.UserService;

public interface UserManager {

    String changeUserAccess(boolean access, User selectedUser, UserService userService);

    String updateUser(String content, UserService userService, ModifyUserDto userDto);
}
