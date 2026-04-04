package org.zeki.virtualtechseller.dto.exhibition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionAccessDto {

    private int idExhibition;
    private LocalDate initDate;
    private LocalDate endDate;
    private boolean active;

}

