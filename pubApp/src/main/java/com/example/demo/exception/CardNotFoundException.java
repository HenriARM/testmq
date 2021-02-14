package com.example.demo.exception;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(String cardNumber) {
        super("Could not find card with cardNumber=" + cardNumber);
    }
}