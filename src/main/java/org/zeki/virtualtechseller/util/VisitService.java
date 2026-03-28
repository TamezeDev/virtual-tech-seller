package org.zeki.virtualtechseller.util;

import org.zeki.virtualtechseller.repository.VisitRepository;

public final class VisitService {

    private final VisitRepository visitRepository;

    public VisitService() {
        this.visitRepository = new VisitRepository();
    }
}
