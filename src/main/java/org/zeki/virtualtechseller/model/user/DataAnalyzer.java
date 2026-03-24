package org.zeki.virtualtechseller.model.user;

import org.zeki.virtualtechseller.model.product.Sale;
import org.zeki.virtualtechseller.model.exhibition.UserVisit;

import java.util.List;

public interface DataAnalyzer {
    List<Sale> showSales();
    void exportSalesToXML();
    List<UserVisit> showVisitors();
    void importVisitorsFromXML();
    void exportVisitorsToXML();

}
