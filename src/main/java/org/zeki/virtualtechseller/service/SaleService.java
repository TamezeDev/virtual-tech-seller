package org.zeki.virtualtechseller.service;

import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.product.Sale;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.model.user.User;
import org.zeki.virtualtechseller.repository.SaleRepository;
import org.zeki.virtualtechseller.util.AlertHelper;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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

    public ResultService<List<Sale>> getTotalSales(Exhibition exhibition) {

        try {
            List<Sale> sales = saleRepository.getTotalSales(exhibition);
            return buildSaleResult(sales);
        } catch (DBConnectionException | SQLException e) {
            return handleVisitError(e);
        }
    }

    public ResultService<List<Sale>> salesBetweenDates(LocalDate initDate, LocalDate endDate) {

        try {
            List<Sale> sales = saleRepository.salesBetweenDates(initDate, endDate);
            return buildSaleResult(sales);
        } catch (DBConnectionException | SQLException e) {
            return handleVisitError(e);
        }
    }

    public int getAmountEarnings(Exhibition exhibition) {

        try {
            if (exhibition == null) {
                return saleRepository.getAmountEarnings();
            } else {
                return saleRepository.getAmountEarnings(exhibition);
            }

        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return -1;
        } catch (SQLException e) {
            String message = "Error adquiriendo el total de ganancias";
            e.printStackTrace();
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return -1;
        }
    }

    private ResultService<List<Sale>> buildSaleResult(List<Sale> sales) {

        if (sales.isEmpty()) {
            return new ResultService<>(false, "La lista de ventas está vacía", sales);
        }
        return new ResultService<>(true, "Mostrando listado de ventas", sales);
    }

    private ResultService<List<Sale>> handleVisitError(Exception e) {

        if (e instanceof DBConnectionException) {
            AlertHelper.showDBConnectAlert();
            return new ResultService<>(false, "Error conectando con el servidor", new ArrayList<>());
        }

        String message = "Error recibiendo listado de visitas";
        AlertHelper.showSQLAlert(message);
        return new ResultService<>(false, message, new ArrayList<>());
    }

}
