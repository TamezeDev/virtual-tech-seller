package org.zeki.virtualtechseller.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@AllArgsConstructor
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "products")
public class ProductsXML {

    public ProductsXML() {
        products = new ArrayList<>();
    }

    @XmlElements({
            @XmlElement(name = "newProduct", type = NewProduct.class),
            @XmlElement(name = "usedProduct", type = UsedProduct.class)
    })
    private List<Product> products;


}
