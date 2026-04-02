package org.zeki.virtualtechseller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitionModifyDto {

    private int idExhibition;
    private String name;
    private String description;
    private LocalDate initDate;
    private LocalDate endDate;
}
