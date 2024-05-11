package com.bridgelabz.bookStore.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorMessage {

    public ErrorMessage() {
    }

    public ErrorMessage(String message) {
        this.message = message;
    }

    private String message;
}
