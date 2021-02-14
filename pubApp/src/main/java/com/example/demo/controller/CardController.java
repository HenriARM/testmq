package com.example.demo.controller;

import com.example.demo.dto.CardDto;
import com.example.demo.exception.CardAlreadyExistsException;
import com.example.demo.exception.CardNotFoundException;
import com.example.demo.message.CreateMessage;
import com.example.demo.message.DeleteMessage;
import com.example.demo.message.ReadMessage;
import com.example.demo.message.UpdateMessage;
import com.example.demo.model.*;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.demo.payroll.CardModelAssembler;
import org.springframework.hateoas.EntityModel;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Validated
public class CardController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CardModelAssembler cardModelAssembler;

    // TODO: create RabbitMqProperties
    public static final String EXCHANGE_NAME = "crud-exchange";

//    @GetMapping("/test/{number}")
//    public ResponseEntity<String> test(@PathVariable() @Size(min = 2, max = 5) @NotBlank String number) {
//        return ResponseEntity.ok("Hello World");
//    }

    @GetMapping("/cards/{cardNumber}")
    public ResponseEntity<EntityModel<CardDto>> retrieveCard(@PathVariable @CreditCardNumber String cardNumber) {
        ReadMessage readMessage = new ReadMessage(cardNumber);
        // Because of one message queue used for both GET one card and GET all cards, 
        // we have only one response type - List of DTO's. If one card requested, get it from first list element
        List<CardDto> cardDtos = cast(rabbitTemplate.convertSendAndReceive(EXCHANGE_NAME, CrudQueue.READ.getQueueName(), readMessage));
        if (cardDtos == null) throw new CardNotFoundException(cardNumber);
        return ResponseEntity
                // Location header added to Response
                .created(linkTo(methodOn(CardController.class).retrieveCard(cardNumber)).toUri()) //
                .body(cardModelAssembler.toModel(cardDtos.get(0)));
    }

    @GetMapping("/cards")
    public ResponseEntity<CollectionModel<EntityModel<CardDto>>> retrieveAllCards() {
        // hardcoded signal to read all Cards
        ReadMessage readMessage = new ReadMessage("all");

        // loop list of cards and convert to model
        List<CardDto> cardDtos = cast(rabbitTemplate.convertSendAndReceive(EXCHANGE_NAME, CrudQueue.READ.getQueueName(), readMessage));

        // no cards        
        if (cardDtos == null) throw new CardNotFoundException("ALL");
        List<EntityModel<CardDto>> entityModels = cardDtos.stream()
                .map(cardModelAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity
                // Location header added to Response
                .created(linkTo(methodOn(CardController.class).retrieveAllCards()).toUri()) //
                .body(CollectionModel.of(entityModels, linkTo(methodOn(CardController.class).retrieveAllCards()).withSelfRel()));
    }

    @PostMapping("/cards")
    public ResponseEntity<EntityModel<CardDto>> createCard(@Valid @RequestBody CardDto cardDto) {
        // send Card DTO as a message
        CreateMessage createMessage = new CreateMessage(cardDto);
        CardDto savedCardDto = (CardDto) rabbitTemplate.convertSendAndReceive(EXCHANGE_NAME, CrudQueue.CREATE.getQueueName(), createMessage);
        if (savedCardDto == null) throw new CardAlreadyExistsException(cardDto.getCardNumber());
        return ResponseEntity
                // Location header added to Response
                .created(linkTo(methodOn(CardController.class).retrieveCard(savedCardDto.getCardNumber())).toUri()) //
                .body(cardModelAssembler.toModel(savedCardDto));
    }

    @PutMapping("/cards/{cardNumber}")
    public ResponseEntity<EntityModel<CardDto>> updateCard(@Valid @RequestBody CardDto newCardDto, @PathVariable @CreditCardNumber String cardNumber) {
        // send Card DTO and Card number as a message
        UpdateMessage updateMessage = new UpdateMessage(cardNumber, newCardDto);
        CardDto updatedCardDto = (CardDto) rabbitTemplate.convertSendAndReceive(EXCHANGE_NAME, CrudQueue.UPDATE.getQueueName(), updateMessage);
        assert updatedCardDto != null;
        return ResponseEntity
                // Location header added to Response
                .created(linkTo(methodOn(CardController.class).retrieveCard(updatedCardDto.getCardNumber())).toUri()) //
                .body(cardModelAssembler.toModel(updatedCardDto));
    }

    @DeleteMapping("/cards/{cardNumber}")
    public ResponseEntity<?> deleteCard(@PathVariable @CreditCardNumber String cardNumber) {
        DeleteMessage deleteMessage = new DeleteMessage(cardNumber);
        if (rabbitTemplate.convertSendAndReceive(EXCHANGE_NAME, CrudQueue.DELETE.getQueueName(), deleteMessage) == null)
            throw new CardNotFoundException(cardNumber);
        return ResponseEntity.noContent().build();
    }

    @SuppressWarnings("unchecked")
    public static <T extends List<?>> T cast(Object obj) {
        return (T) obj;
    }
}