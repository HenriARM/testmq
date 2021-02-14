package com.example.demo.message;

import com.example.demo.dto.CardDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public final class CreateMessage implements Serializable {
    private final CardDto cardDto;

    public CreateMessage(@JsonProperty("cardDto") CardDto cardDto) {
        this.cardDto = cardDto;
    }

    public CardDto getCardDto() {
        return cardDto;
    }

    @Override
    public String toString() {
        return cardDto.toString();
    }
}
