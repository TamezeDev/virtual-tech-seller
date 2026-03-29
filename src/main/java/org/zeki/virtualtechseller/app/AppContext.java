package org.zeki.virtualtechseller.app;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.zeki.virtualtechseller.database.ConnectionManager;
import org.zeki.virtualtechseller.service.*;
import org.zeki.virtualtechseller.util.VisitService;

@Getter
@Setter
@AllArgsConstructor
public final class AppContext {

    private static AppContext INSTANCE;

    private final ConnectionManager connectionManager;

    private UserService userService;
    private ProductService productService;
    private ExhibitionService exhibitionService;
    private CartService cartService;
    private SaleService saleService;
    private VisitService visitService;

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

    public CartService getCartService() {
        if (cartService == null) {
            cartService = new CartService();
        }
        return cartService;
    }

    public SaleService getSaleService() {
        if (this.saleService == null) {
            saleService = new SaleService();
        }
        return saleService;
    }

    public VisitService getVisitService() {
        if (this.visitService == null) {
            visitService = new VisitService();
        }
        return visitService;
    }
}
