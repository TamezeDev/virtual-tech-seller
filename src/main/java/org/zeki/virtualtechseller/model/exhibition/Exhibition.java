package org.zeki.virtualtechseller.model.exhibition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.zeki.virtualtechseller.model.product.Product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Exhibition {
    private int idExhibition;
    private String name;
    private String description;
    private LocalDate initDate;
    private LocalDate endDate;
    private boolean active;
    private List<ExhibitionItem> items;

    public Exhibition() {
        items = new ArrayList<>();
    }

    public String checkActive() {
        return active ? "Activada" : "Desactivada";
    }

    public void enable() {
        this.active = true;
    }

    public void disable() {
        this.active = false;
    }

    @Override
    public String toString() {
        return name;
    }
}
