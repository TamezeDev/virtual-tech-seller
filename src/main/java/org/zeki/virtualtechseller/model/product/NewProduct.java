package org.zeki.virtualtechseller.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public final class NewProduct extends Product {

    private int stock;
    @XmlElement(name = "releaseDate")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate releaseDate;

    public boolean hasStock(int quantity) {
        return stock >= quantity;
    }

    @Override
    public double calculateUnitPrice() {
        return getBasePrice() * (1 + (getTaxPercentage() / 100));
    }
}
