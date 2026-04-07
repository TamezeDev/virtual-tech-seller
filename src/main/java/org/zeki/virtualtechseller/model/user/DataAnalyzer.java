package org.zeki.virtualtechseller.model.user;

import javafx.scene.control.Label;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.product.Sale;
import org.zeki.virtualtechseller.model.exhibition.UserVisit;
import org.zeki.virtualtechseller.service.SaleService;
import org.zeki.virtualtechseller.service.VisitService;

import java.time.LocalDate;
import java.util.List;

public interface DataAnalyzer {

    int amountEarnings(SaleService service, Exhibition exhibition);

    List<Sale> filterSales(SaleService service, Label feedBack, Label counter, Exhibition exhibition);

    List<Sale> filterSales(SaleService service, Label feedBack, Label counter, LocalDate initDate, LocalDate endDate);

    List<UserVisit> filterGroupEvent(VisitService service, Label feedBack);

    List<UserVisit> filterVisitors(VisitService service, Label feedBack, Label counter, int minValue, int maxValue);

    int amountVisits(VisitService service, Exhibition exhibition);

    List<UserVisit> filterVisitors(VisitService service, Label feedBack, Label counter, Exhibition exhibition);

}
