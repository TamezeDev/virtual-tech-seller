package org.zeki.virtualtechseller.service;

import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.exhibition.ExhibitionItem;
import org.zeki.virtualtechseller.model.product.*;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.model.user.User;
import org.zeki.virtualtechseller.repository.*;
import org.zeki.virtualtechseller.util.AlertHelper;
import org.zeki.virtualtechseller.util.TransactionHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;

    public CartService() {
        this.cartRepository = new CartRepository();
        this.userRepository = new UserRepository();
        this.exhibitionRepository = new ExhibitionRepository();
        this.productRepository = new ProductRepository();
        this.saleRepository = new SaleRepository();
    }

    public void setCartItemList(User currentUser) {
        try {
            List<CartItem> cartItems = cartRepository.getCartItem(currentUser);
            if (cartItems != null) {


                ((Client) currentUser).setCartItems(cartItems);
            }

        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
        } catch (SQLException e) {
            String message = "Error obteniendo datos de la cesta del usuario";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
        }
    }

    public boolean removeSingleCartItemFromDb(CartItem cartItem) {
        try {

            Client client = (Client) SessionManager.getInstance().getCurrentUser();
            return cartRepository.removeUserCartItem(cartItem, client);

        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return false;

        } catch (SQLException e) {
            String message = "Error eliminando del servidor producto del carrito";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return false;
        }
    }

    public boolean removeAllCartItemsFromDB() {
        try {
            Client client = (Client) SessionManager.getInstance().getCurrentUser();
            return cartRepository.removeAllUserCartItems(client);

        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return false;

        } catch (SQLException e) {
            String message = "Error eliminando del servidor los productos de la cesta del usuario";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return false;
        }
    }

    public boolean addToCartItem(ExhibitionItem item, int quantity) {
        Client client = (Client) SessionManager.getInstance().getCurrentUser();

        try {
            return cartRepository.saveCartItem(
                    client.getIdUser(),
                    item.getProduct().getIdProduct(),
                    client.getCurrentExhibition().getIdExhibition(),
                    quantity
            );

        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert();
            return false;

        } catch (SQLException e) {
            AlertHelper.showSQLAlert("Error actualizando productos del carrito");
            return false;
        }
    }

    public ResultService<Void> checkoutCart(List<CartItem> items, double amount) {
        if (items == null || items.isEmpty()) {
            return new ResultService<>(false, "El carrito está vacío");
        }

        Client client = (Client) SessionManager.getInstance().getCurrentUser();
        Connection connection = null;

        try {
            connection = TransactionHelper.begin(AppContext.getInstance().getConnectionManager());

            ResultService<Void> result = processCheckout(connection, client, items, amount);
            if (!result.isSuccess()) {
                TransactionHelper.rollback(connection);
                return result;
            }

            connection.commit();
            return new ResultService<>(true, "Compra realizada correctamente");

        } catch (DBConnectionException e) {
            TransactionHelper.rollback(connection);
            AlertHelper.showDBConnectAlert();
            return new ResultService<>(false, "Error de conexión");

        } catch (SQLException e) {
            TransactionHelper.rollback(connection);
            AlertHelper.showSQLAlert("Error en la transacción de compra");
            e.printStackTrace();
            return new ResultService<>(false, "No se pudo completar la transacción");

        } finally {
            TransactionHelper.close(connection);
        }
    }

    private ResultService<Void> processCheckout(Connection connection, Client client, List<CartItem> items, double amount) throws SQLException, DBConnectionException {

        int rsCredit = userRepository.decreaseCredit(connection, amount, client.getIdUser());
        if (rsCredit == 0) {
            return new ResultService<>(false, "Crédito insuficiente para realizar la compra");
        }

        for (CartItem item : items) {
            ResultService<Void> result = processItemPurchase(connection, item);
            if (!result.isSuccess()) {
                return result;
            }
        }

        List<Sale> sales = client.buyCart(items);
        for (Sale sale : sales) {
            if (!saleRepository.saveSale(connection, sale)) {
                return new ResultService<>(false, "No se pudo guardar una venta en el servidor");
            }
        }

        if (!cartRepository.removeAllUserCartItems(connection, client.getIdUser())) {
            return new ResultService<>(false, "No se pudo vaciar la cesta");
        }

        return new ResultService<>(true, "OK");
    }

    private ResultService<Void> processItemPurchase(Connection connection, CartItem item) throws SQLException, DBConnectionException {

        int rsStockExhibition = exhibitionRepository.decreaseExhibitionStock(connection, item.getProduct().getIdProduct(), item.getExhibition().getIdExhibition(), item.getQuantity());

        if (rsStockExhibition == 0) {
            return new ResultService<>(false, "No hay stock del producto " + item.getProduct().getName() + " en el evento " + item.getExhibition().getName());
        }

        if (item.getProduct() instanceof NewProduct) {
            int rsStockNewProduct = productRepository.decreaseNewProductStock(connection, item.getQuantity(), item.getProduct().getIdProduct());

            if (rsStockNewProduct == 0) {
                return new ResultService<>(false, "No hay stock global del producto " + item.getProduct().getName());
            }

            if (productRepository.checkSoldOutNewProduct(connection, item.getProduct().getIdProduct())) {
                productRepository.setNoAvailableProduct(connection, item.getProduct().getIdProduct());
            }
        }

        if (item.getProduct() instanceof UsedProduct) {
            int rsAvailableProduct = productRepository.setNoAvailableProduct(connection, item.getProduct().getIdProduct());

            if (rsAvailableProduct == 0) {
                return new ResultService<>(false, "El producto ya no está disponible: " + item.getProduct().getName());
            }
        }

        return new ResultService<>(true, "OK");
    }


}

