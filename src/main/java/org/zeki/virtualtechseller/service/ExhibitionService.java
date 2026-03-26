package org.zeki.virtualtechseller.service;

import org.zeki.virtualtechseller.repository.ExhibitionRepository;

public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;

    public ExhibitionService() {
        this.exhibitionRepository = new ExhibitionRepository();
    }
}
