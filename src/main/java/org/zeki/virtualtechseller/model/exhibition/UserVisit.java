package org.zeki.virtualtechseller.model.exhibition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zeki.virtualtechseller.model.user.Client;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserVisit {

    private Client client;
    private Exhibition exhibition;
    private LocalDate lastVisit;
    private int visitCounter;

    public void increaseVisit() {
        visitCounter += 1;
    }

    public void updateLastVisit() {
        this.lastVisit = LocalDate.now();
    }

}
