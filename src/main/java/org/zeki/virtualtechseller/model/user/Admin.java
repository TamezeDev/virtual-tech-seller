package org.zeki.virtualtechseller.model.user;

import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zeki.virtualtechseller.dto.exhibition.ExhibitionModifyDto;
import org.zeki.virtualtechseller.dto.exhibition.ExhibitionProductsDto;
import org.zeki.virtualtechseller.dto.product.NewProductDto;
import org.zeki.virtualtechseller.dto.product.UsedProductDto;
import org.zeki.virtualtechseller.dto.user.ModifyUserDto;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.exhibition.ExhibitionItem;
import org.zeki.virtualtechseller.service.ExhibitionService;
import org.zeki.virtualtechseller.service.ProductService;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public final class Admin extends User implements ProductManager, UserManager, ExhibitionManager {

    @Override
    public void assignProductToExhibition(ObservableList<ExhibitionProductsDto> productsDto, ExhibitionProductsDto selectedProductDto, Exhibition selectedExhibicion, int quantity) {
        int productId = selectedProductDto.getProduct().getIdProduct();
        int exhibitionId = selectedExhibicion.getIdExhibition();

        Optional<ExhibitionProductsDto> result = productsDto.stream().filter(item -> item.getProduct() != null
                && item.getProduct().getIdProduct() == productId && item.getExhibition() != null && item.getExhibition().getIdExhibition() == exhibitionId).findFirst();

        if (result.isPresent()) {
            result.get().getExhibitionItem().increaseQuantity(quantity);
            return;
        }

        ExhibitionItem exhibitionItem = new ExhibitionItem();
        exhibitionItem.setProduct(selectedProductDto.getProduct());
        exhibitionItem.setQuantity(quantity);
        ExhibitionProductsDto newDto = new ExhibitionProductsDto();
        newDto.setProduct(selectedProductDto.getProduct());
        newDto.setExhibition(selectedExhibicion);
        newDto.setExhibitionItem(exhibitionItem);
        productsDto.add(newDto);
    }

    @Override
    public void decreaseProductFromExhibition(ExhibitionProductsDto selectedProductDto, int quantity) {
        selectedProductDto.getExhibitionItem().decreaseQuantity(quantity);
    }

    @Override
    public void retireProductFromExhibition(ExhibitionProductsDto selectedProductDto) {
        selectedProductDto.setExhibition(null);
        selectedProductDto.setExhibitionItem(null);
    }

    @Override
    public String createProduct(ProductService service, NewProductDto newProductDto, UsedProductDto usedProductDto) {
        return service.addNewProduct(newProductDto, usedProductDto).getMessage();
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
