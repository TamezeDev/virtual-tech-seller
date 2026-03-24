package org.zeki.virtualtechseller.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zeki.virtualtechseller.model.product.Sale;
import org.zeki.virtualtechseller.model.exhibition.UserVisit;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class Moderator extends User implements DataAnalyzer {

    @Override
    public List<Sale> showSales() {
        return List.of();
    }

    @Override
    public void exportSalesToXML() {

    }

    @Override
    public List<UserVisit> showVisitors() {
        return List.of();
    }

    @Override
    public void importVisitorsFromXML() {

    }

    @Override
    public void exportVisitorsToXML() {

    }

    @Override
    public Role getRoleName() {
        return Role.MODERATOR;
    }
}
