package com.example.demo.exception;

public class CardAlreadyExistsException extends RuntimeException {
    public CardAlreadyExistsException(String cardNumber) {
        super("Card with cardNumber=" + cardNumber + " already exists");
    }
}