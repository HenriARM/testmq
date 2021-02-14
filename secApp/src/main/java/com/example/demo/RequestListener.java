package com.example.demo;

import com.example.demo.dto.CardTransfer;
import com.example.demo.message.CreateMessage;
import com.example.demo.message.DeleteMessage;
import com.example.demo.message.ReadMessage;
import com.example.demo.message.UpdateMessage;
import com.example.demo.model.*;
import com.example.demo.dto.CardDto;

import com.example.demo.repository.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// can also be used for abstract message, to read headers
//import org.springframework.messaging.Message;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestListener {

    private static final Logger log = LoggerFactory.getLogger(RequestListener.class);

    @Autowired
    private CardRepository cardRepository;

    @RabbitListener(queues = {CrudQueue.Constants.CREATE_VALUE})
    public CardDto createCard(CreateMessage createMessage) {
        Card card = CardTransfer.toEntity(createMessage.getCardDto());
        if (cardRepository.findCardByCardNumber(card.getCardNumber()).isPresent()) return null;
        Card savedCard = cardRepository.save(card);
        return CardTransfer.toDto(savedCard);
    }

    @RabbitListener(queues = {CrudQueue.Constants.READ_VALUE})
    public List<CardDto> readCards(ReadMessage readMessage) {
        // TODO: add trace logs
        String cardNumber = readMessage.getCardNumber();
        if ("all".equals(cardNumber)) {
            // get All Cards
            List<Card> cards = cardRepository.findAll();
            if (cards.isEmpty()) return null;
            return cards.stream()
                    .map(CardTransfer::toDto)
                    .collect(Collectors.toList());
        } else {
            // get specific Card
            Optional<Card> cardOptional = cardRepository.findCardByCardNumber(readMessage.getCardNumber());
            if (cardOptional.isEmpty()) return null;
            else {
                List<CardDto> cardDtos = new ArrayList<>();
                cardDtos.add(CardTransfer.toDto(cardOptional.get()));
                return cardDtos;
            }

        }
    }

    @RabbitListener(queues = {CrudQueue.Constants.UPDATE_VALUE})
    public CardDto updateCard(UpdateMessage updateMessage) {
        Card newCard = CardTransfer.toEntity(updateMessage.getCardDto());
        String cardNumber = updateMessage.getCardNumber();

        Card updatedCard = cardRepository.findCardByCardNumber(cardNumber)
                .map(oldCard -> {
                    oldCard.setFirstName(newCard.getFirstName());
                    oldCard.setLastName(newCard.getLastName());
                    return cardRepository.save(oldCard);
                })
                .orElseGet(() -> cardRepository.save(newCard));
        return CardTransfer.toDto(updatedCard);
    }

    @Transactional
    @RabbitListener(queues = {CrudQueue.Constants.DELETE_VALUE})
    public Long deleteCard(DeleteMessage deleteMessage) {
        String cardNumber = deleteMessage.getCardNumber();
        if (cardRepository.findCardByCardNumber(cardNumber).isEmpty())
            return null;
        return cardRepository.deleteByCardNumber(cardNumber);
    }
}