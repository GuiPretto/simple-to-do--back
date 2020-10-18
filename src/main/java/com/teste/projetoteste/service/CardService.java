package com.teste.projetoteste.service;

import java.util.List;
import java.util.Optional;

import com.teste.projetoteste.model.entity.Card;
import com.teste.projetoteste.model.entity.Container;

public interface CardService {
	
	Card createCard(Card card);
	
	Card updateCard(Card card);
	
	void deleteCard(Card card);
	
	List<Card> getAllWithContainerId(Container containerFilter);
	
	void validateNewCard(Card card);
	
	void validateExistentCard(Card card);
	
	Optional<Card> getById(int id);
}
