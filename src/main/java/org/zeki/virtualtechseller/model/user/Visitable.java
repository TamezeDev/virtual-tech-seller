package org.zeki.virtualtechseller.model.user;

import org.zeki.virtualtechseller.model.exhibition.Exhibition;

public interface Visitable {

    void updateUserVisit(Exhibition exhibition, Client client);
}
