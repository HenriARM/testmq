package com.example.demo.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public final class DeleteMessage implements Serializable {
    private final String cardNumber;

    public DeleteMessage(@JsonProperty("cardNumber") String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    @Override
    public String toString() {
        return "ReadMessage {" +
                "cardNumber='" + cardNumber + "'" +
                "}";
    }
}
