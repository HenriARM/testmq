package com.example.demo.model;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;

import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    // always 16 digits
    @Column(nullable = false, length = 16)
    @CreditCardNumber(message = "invalid credit card number")
    private String cardNumber;

    // latin characters, with size 2-30
    @Column(nullable = false, length = 30)
    @Pattern(regexp = "\\p{IsLatin}+", message = "firstName should only be from Latin characters")
    @Length(min=2, max =30, message ="firstName size 2-30 characters")
    private String firstName;

    // latin characters, with size 2-30
    @Column(nullable = false, length = 30)
    @Pattern(regexp = "\\p{IsLatin}+", message = "lastName should only be from Latin characters")
    @Length(min=2, max =30, message ="lastName size 2-30 characters")
    private String lastName;

    public Card() {
    }

    public Card(String cardNumber, String firstName, String lastName) {
        this.cardNumber = cardNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Card))
            return false;
        Card card = (Card) o;
        return Objects.equals(this.id, card.id) && Objects.equals(this.cardNumber, card.cardNumber)
                && Objects.equals(this.firstName, card.firstName)
                && Objects.equals(this.lastName, card.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.cardNumber, this.firstName, this.lastName);
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + this.id + "', " +
                "cardNumber='" + this.cardNumber + "', " +
                "firstName='" + this.firstName + "', " +
                "lastName='" + this.lastName + "'" +
                '}';
    }
}