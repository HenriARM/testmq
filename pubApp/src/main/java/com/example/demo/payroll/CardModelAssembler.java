package com.example.demo.payroll;

import com.example.demo.controller.CardController;
import com.example.demo.dto.CardDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class CardModelAssembler implements RepresentationModelAssembler<CardDto, EntityModel<CardDto>> {

    @Override
    public EntityModel<CardDto> toModel(CardDto cardDto) {
        return EntityModel.of(cardDto,
                linkTo(methodOn(CardController.class).retrieveCard(cardDto.getCardNumber())).withSelfRel(),
                linkTo(methodOn(CardController.class).retrieveAllCards()).withRel("allCards"));
    }


}