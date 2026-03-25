package org.zeki.virtualtechseller.service;

import org.zeki.virtualtechseller.repository.ExhibitionRepository;

public class ExhibitionService {
    private ExhibitionRepository exhibitionRepository;

    public ExhibitionService(ExhibitionRepository exhibitionRepository) {
        this.exhibitionRepository = exhibitionRepository;
    }
}
