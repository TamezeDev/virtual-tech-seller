package org.zeki.virtualtechseller.app;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zeki.virtualtechseller.database.ConnectionManager;
import org.zeki.virtualtechseller.repository.ExhibitionRepository;
import org.zeki.virtualtechseller.repository.ProductRepository;
import org.zeki.virtualtechseller.repository.UserRepository;
import org.zeki.virtualtechseller.service.ExhibitionService;
import org.zeki.virtualtechseller.service.ProductService;
import org.zeki.virtualtechseller.service.UserService;

@Getter
@Setter
@AllArgsConstructor
public final class AppContext {

    private static AppContext INSTANCE;

    private final ConnectionManager connectionManager;

    private UserService userService;
    private ProductService productService;
    private ExhibitionService exhibitionService;

    public AppContext() {
        connectionManager = new ConnectionManager();
    }

    public static AppContext getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppContext();
        }
        return INSTANCE;
    }

    public UserService getUserService() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }

    public ProductService getProductService() {
        if (productService == null) {
            productService = new ProductService();
        }
        return productService;
    }

    public ExhibitionService getExhibitionService() {
        if (exhibitionService == null) {
            exhibitionService = new ExhibitionService();
        }
        return exhibitionService;
    }
}
