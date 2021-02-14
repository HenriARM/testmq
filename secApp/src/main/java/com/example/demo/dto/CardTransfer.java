package com.example.demo.dto;

import com.example.demo.model.Card;

public class CardTransfer {
    public static CardDto toDto(Card card) {
        return new CardDto(card.getCardNumber(), card.getFirstName(), card.getLastName());
    }
    public static Card toEntity(CardDto cardDto) {
        return new Card(cardDto.getCardNumber(), cardDto.getFirstName(), cardDto.getLastName());
    }
}
