package com.example.demo.repository;

import com.example.demo.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findCardByCardNumber(String cardNumber);
    Long deleteByCardNumber(String cardNumber);
}
