package org.zeki.virtualtechseller.model.exhibition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.zeki.virtualtechseller.model.product.Product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Exhibition {
    private int idExhibition;
    private String name;
    private String description;
    private LocalDate initDate;
    private LocalDate endDate;
    private boolean active;
    private List<ExhibitionItem> items;

    public Exhibition() {
        items = new ArrayList<>();
    }

    public void addProduct(Product product, int quantity) {
    }

    public void removeProduct(Product product) {
    }

    public boolean containsProduct(Product product) {
        return items.stream().anyMatch(item -> item.getProduct().getIdProduct() == product.getIdProduct());
    }

    public void enable() {
        this.active = true;
    }

    public void disable() {
        this.active = false;
    }

    public boolean isActive() {
        return active;
    }

}
