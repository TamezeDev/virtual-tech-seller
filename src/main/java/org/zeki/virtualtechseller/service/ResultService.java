package org.zeki.virtualtechseller.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResultService<T> {
    private boolean success;
    private String message;
    private T data;

    public ResultService(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

}
