package org.zeki.virtualtechseller.model.user;

import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.service.ExhibitionService;

public interface ExhibitionManager {
    String createExhibition(Exhibition exhibition, ExhibitionService exhibitionService);

    void enableExhibition(Exhibition exhibition);

    void disableExhibition(Exhibition exhibition);
}
