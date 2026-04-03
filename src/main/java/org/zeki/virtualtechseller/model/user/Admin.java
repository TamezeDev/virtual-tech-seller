package org.zeki.virtualtechseller.model.user;

import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zeki.virtualtechseller.dto.exhibition.ExhibitionModifyDto;
import org.zeki.virtualtechseller.dto.product.NewProductDto;
import org.zeki.virtualtechseller.dto.product.UsedProductDto;
import org.zeki.virtualtechseller.dto.user.ModifyUserDto;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.product.Product;
import org.zeki.virtualtechseller.service.ExhibitionService;
import org.zeki.virtualtechseller.service.ProductService;
import org.zeki.virtualtechseller.service.ResultService;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public final class Admin extends User implements ProductManager, UserManager, ExhibitionManager {

    @Override
    public String createProduct(ProductService service, NewProductDto newProductDto, UsedProductDto usedProductDto) {
       return service.addNewProduct(newProductDto,usedProductDto).getMessage();
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
    public void changeUserAccess(ObservableList<User> users, User selectedUser, boolean status) {
        Optional<User> foundUser = users.stream().filter(user -> user.getEmail().equals(selectedUser.getEmail())).findFirst();
        foundUser.ifPresent(user -> user.setEmailActivate(status));
    }

    @Override
    public void updateUser(User selectedUser, ModifyUserDto userDto) {
        selectedUser.setName(userDto.getName());
        selectedUser.setLastName(userDto.getLastName());
        selectedUser.setPhone(userDto.getPhone());
        selectedUser.setEmail(userDto.getPhone());
    }

    @Override
    public String createExhibition(Exhibition exhibition, ExhibitionService exhibitionService) {
        return exhibitionService.addNewExhibition(exhibition).getMessage();
    }

    @Override
    public void enableExhibition(Exhibition exhibition) {
        exhibition.enable();
    }

    @Override
    public void disableExhibition(Exhibition exhibition) {
        exhibition.disable();

    }

    @Override
    public String modifyExhibition(ExhibitionService service, ExhibitionModifyDto exhibitionModifyDto) {
        return service.modifyExhibition(exhibitionModifyDto);
    }

    @Override
    public UserRole getRoleName() {
        return UserRole.ADMIN;
    }
}
