package org.zeki.virtualtechseller.service;

import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.product.Sale;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.model.user.User;
import org.zeki.virtualtechseller.repository.SaleRepository;
import org.zeki.virtualtechseller.util.AlertHelper;

import java.sql.SQLException;
import java.util.List;

public class SaleService {

    private final SaleRepository saleRepository;

    public SaleService() {
        this.saleRepository = new SaleRepository();
    }

    public void setSalesList(User currentUser) {

        try {
            List<Sale> userSales = saleRepository.getSalesByUser(currentUser);

            if (userSales != null || !userSales.isEmpty()) {
                ((Client) currentUser).setSales(userSales);
            }

        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
        } catch (SQLException e) {
            String message = "Error obteniendo las compras del usuario";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
        }
    }

}
