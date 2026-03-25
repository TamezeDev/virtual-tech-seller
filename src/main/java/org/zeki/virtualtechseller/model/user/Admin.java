package org.zeki.virtualtechseller.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.product.Product;

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
    public void activateUser(User user) {

    }

    @Override
    public void deactivateUser(User user) {

    }

    @Override
    public void updateUser(User user) {

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
