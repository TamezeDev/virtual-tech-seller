package org.zeki.virtualtechseller.service;

import javafx.scene.control.Label;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.product.Sale;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.model.user.User;
import org.zeki.virtualtechseller.repository.SaleRepository;

import java.sql.SQLException;
import java.util.List;

public class SaleService {

    private final SaleRepository saleRepository;

    public SaleService() {
        this.saleRepository = new SaleRepository();
    }

    public void setSalesList(User currentUser) throws SQLException, DBConnectionException {
        List<Sale> userSales = saleRepository.getSalesByUser(currentUser);
        if (userSales != null || !userSales.isEmpty()) {
            ((Client) currentUser).setSales(userSales);
        }
    }

}
