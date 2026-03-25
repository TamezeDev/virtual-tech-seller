package org.zeki.virtualtechseller.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResultService<T> {
    private final boolean success;
    private final String message;
    private final T data;
}
