package com.example.demo.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Pattern;


public final class CardDto implements Serializable {
    
    @CreditCardNumber(message = "invalid credit card number")
    private final String cardNumber;

    @Pattern(regexp = "\\p{IsLatin}+", message = "firstName should only be from Latin characters")
    @Length(min=2, max =30, message ="firstName size 2-30 characters")
    private final String firstName;

    @Pattern(regexp = "\\p{IsLatin}+", message = "lastName should only be from Latin characters")
    @Length(min=2, max =30, message ="lastName size 2-30 characters")
    private final String lastName;

    public CardDto(@JsonProperty("cardNumber") String cardNumber,
                   @JsonProperty("firstName") String firstName,
                   @JsonProperty("lastName") String lastName) {
        this.cardNumber = cardNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "Card {" +
                "cardNumber='" + cardNumber + "', " +
                "firstName='" + firstName + "', " +
                "lastName='" + lastName + "'" +
                "}";
    }

//    public void setCardNumber(String cardNumber) {
//        this.cardNumber = cardNumber;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
}
