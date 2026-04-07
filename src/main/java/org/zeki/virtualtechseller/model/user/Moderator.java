package org.zeki.virtualtechseller.model.user;

import javafx.scene.control.Label;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.product.Sale;
import org.zeki.virtualtechseller.model.exhibition.UserVisit;
import org.zeki.virtualtechseller.service.ResultService;
import org.zeki.virtualtechseller.service.SaleService;
import org.zeki.virtualtechseller.service.VisitService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public final class Moderator extends User implements DataAnalyzer {
    @Override
    public List<Sale> filterSales(SaleService service, Label feedBack, Label counter, Exhibition exhibition) {
        // GET SALES LIST
        ResultService<List<Sale>> result = service.getTotalSales(exhibition);
        feedBack.setText(result.getMessage());
        // IF NOT EMPTY LIST GET AMOUNT EARNINGS
        if (result.isSuccess()) {
            int amount = amountEarnings(service, exhibition);
            if (amount == -1) {
                feedBack.setText("Error recibiendo sumatorio total de ganancias");
            }
            counter.setText(amount + " €");
            return result.getData();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Sale> filterSales(SaleService service, Label feedBack, Label counter, LocalDate initDate, LocalDate endDate) {
        // GET SALES LIST
        ResultService<List<Sale>> result = service.salesBetweenDates(initDate, endDate);
        feedBack.setText(result.getMessage());
        // IF NOT EMPTY LIST COUNT EARNINGS SALES
        if (result.isSuccess()) {
            List<Sale> sales = result.getData();
            counter.setText(String.valueOf(sales.stream().mapToDouble(Sale::getTotalPrice).sum()));
            return result.getData();
        }
        return new ArrayList<>();
    }

    @Override
    public int amountEarnings(SaleService service, Exhibition exhibition) {
        return service.getAmountEarnings(exhibition);
    }

    @Override
    public int amountVisits(VisitService service, Exhibition exhibition) {
        return service.getAmountVisitors(exhibition);
    }

    @Override
    public List<UserVisit> filterGroupEvent(VisitService service, Label feedBack) {
        // GET VISIT LIST
        ResultService<List<UserVisit>> result = service.visitsGroupByEvent();
        feedBack.setText(result.getMessage());
        // IF NOT EMPTY LIST COUNT VISITS
        if (result.isSuccess()) {
            List<UserVisit> visits = result.getData();
            return result.getData();
        }
        return new ArrayList<>();
    }

    @Override
    public List<UserVisit> filterVisitors(VisitService service, Label feedBack, Label counter, int minValue, int maxValue) {
        // GET VISIT LIST
        ResultService<List<UserVisit>> result = service.visitsByNumAccessOrdered(minValue, maxValue);
        feedBack.setText(result.getMessage());
        // IF NOT EMPTY LIST COUNT VISITS
        if (result.isSuccess()) {
            List<UserVisit> visits = result.getData();
            counter.setText(String.valueOf(visits.stream().mapToInt(UserVisit::getVisitCounter).sum()));
            return result.getData();
        }
        return new ArrayList<>();
    }

    @Override
    public List<UserVisit> filterVisitors(VisitService service, Label feedBack, Label counter, Exhibition exhibition) {
        // GET VISIT LIST
        ResultService<List<UserVisit>> result = service.getVisitors(exhibition);
        feedBack.setText(result.getMessage());
        // IF NOT EMPTY LIST COUNT VISITS
        if (result.isSuccess()) {
            int amount = amountVisits(service, exhibition);
            if (amount == -1) {
                feedBack.setText("Error recibiendo sumatorio total de visitas");
            }
            counter.setText(String.valueOf(amount));
            return result.getData();
        }
        return new ArrayList<>();
    }

    @Override
    public UserRole getRoleName() {
        return UserRole.MODERATOR;
    }
}
