package com.example.demo.message;

import com.example.demo.dto.CardDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public final class UpdateMessage implements Serializable {
    private final String cardNumber;
    private final CardDto cardDto;

    public UpdateMessage(@JsonProperty("cardNumber") String cardNumber, @JsonProperty("cardDto") CardDto cardDto) {
        this.cardNumber = cardNumber;
        this.cardDto = cardDto;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public CardDto getCardDto() {
        return cardDto;
    }

    @Override
    public String toString() {
        return cardDto.toString();
    }
}
