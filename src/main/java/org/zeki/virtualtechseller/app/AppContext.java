package org.zeki.virtualtechseller.app;

import lombok.AllArgsConstructor;
import lombok.Getter;
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

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private ExhibitionRepository exhibitionRepository;

    private UserService userService;
    private ProductService productService;
    private ExhibitionService exhibitionService;

    private AppContext() {
        connectionManager = new ConnectionManager();
    }

    public static AppContext getInstance() {
        if (INSTANCE == null){
            INSTANCE = new AppContext();
        }
        return INSTANCE;
    }

    public UserService getUserService() {
        if (userService == null) {
            if (userRepository == null) userRepository = new UserRepository(connectionManager);
            userService = new UserService(userRepository);
        }
        return userService;
    }

    public ProductService getProductService() {
        if (productService == null) {
            if (productRepository == null) productRepository = new ProductRepository(connectionManager);
            productService = new ProductService(productRepository);
        }
        return productService;
    }

    public ExhibitionService getExhibitionService() {
        if (exhibitionService == null) {
            if (exhibitionRepository == null) exhibitionRepository = new ExhibitionRepository(connectionManager);
            exhibitionService = new ExhibitionService(exhibitionRepository);
        }
        return exhibitionService;
    }
}
