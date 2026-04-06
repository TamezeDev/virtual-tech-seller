package org.zeki.virtualtechseller.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Product {

    @XmlAttribute
    protected int idProduct;
    protected String name;
    protected String description;
    protected String urlImage;
    protected double basePrice;
    private double taxPercentage;
    protected boolean available;
    @XmlElement(name = "category")
    protected Category category;

    public Product() {
        taxPercentage = 21.0; // PRODUCTS TAXES IN SPAIN 21%
    }

    public boolean belongToCategory(Category category) {
        return this.category.getIdCategory() == category.getIdCategory();
    }

    public abstract double calculateUnitPrice();


}
