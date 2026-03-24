package org.zeki.virtualtechseller.model.user;

import org.zeki.virtualtechseller.model.exhibition.Exhibition;

public interface ExhibitionManager {
    void createExhibition(Exhibition exhibition);

    void enableExhibition(Exhibition exhibition);

    void disableExhibition(Exhibition exhibition);
}
