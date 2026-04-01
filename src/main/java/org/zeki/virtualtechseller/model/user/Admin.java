package org.zeki.virtualtechseller.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zeki.virtualtechseller.dto.AccessUserDto;
import org.zeki.virtualtechseller.dto.ModifyUserDto;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.product.Product;
import org.zeki.virtualtechseller.service.UserService;
import org.zeki.virtualtechseller.util.AlertHelper;
import org.zeki.virtualtechseller.util.Feedback;

@Getter
@Setter
@NoArgsConstructor
public final class Admin extends User implements ProductManager, UserManager, ExhibitionManager {

    @Override
    public void createProduct(Product product) {

    }

    @Override
    public void modifyProduct(Product currentProduct, Product newDataProduct) {
        currentProduct.setName(newDataProduct.getName());
        currentProduct.setDescription(newDataProduct.getDescription());
        currentProduct.setBasePrice(newDataProduct.getBasePrice());
        currentProduct.setAvailable(newDataProduct.isAvailable());
    }

    @Override
    public void assignProductToExhibition(Product product, Exhibition exhibition, int quantity) {
        exhibition.addProduct(product, quantity);
    }

    @Override
    public void retireProductFromExhibition(Product product, Exhibition exhibition) {
        exhibition.removeProduct(product);
    }

    @Override
    public String changeUserAccess(boolean access, User selectedUser, UserService userService) {
        // CREATE USER DTO AND MODIFY ACTIVATE USER DB
        if (selectedUser == null) {
            return "Para esta operación debe seleccionar un usuario";
        }
        AccessUserDto userAccessDto = new AccessUserDto(selectedUser.getEmail(), access);
        if (!userService.changeActivateUSer(userAccessDto)) {
            return "Error modificando acceso al usuario";
        } else {
            return "OK";
        }
    }

    @Override
    public String updateUser(String content, UserService userService, ModifyUserDto userDto) {
        // REQUEST CONFIRM NEW CHANGES
        String alertTitle = "Modificación de usuario";
        if (AlertHelper.choiceAlert(alertTitle, content)) {
            return userService.modifyUser(userDto);
        } else return "Operación cancelada por el administrador";
    }

    @Override
    public void createExhibition(Exhibition exhibition) {
        exhibition.enable();
    }

    @Override
    public void enableExhibition(Exhibition exhibition) {
    }

    @Override
    public void disableExhibition(Exhibition exhibition) {
        exhibition.disable();

    }

    @Override
    public Role getRoleName() {
        return Role.ADMIN;
    }
}
