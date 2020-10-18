package com.teste.projetoteste.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teste.projetoteste.api.dto.CardDTO;
import com.teste.projetoteste.exception.CardException;
import com.teste.projetoteste.model.entity.Card;
import com.teste.projetoteste.model.entity.Container;
import com.teste.projetoteste.service.CardService;
import com.teste.projetoteste.service.ContainerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardResource {
	
	private final CardService service;
	private final ContainerService containerService;
	
	
	@PostMapping("/create")
	public ResponseEntity create(@RequestBody CardDTO dto) {
		try {
			Card card = convert(dto);
			card = service.createCard(card);
			return new ResponseEntity(card, HttpStatus.CREATED);
		} catch (CardException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
		
	@PutMapping("/update/{id}")
	public ResponseEntity update(@PathVariable("id") int id, @RequestBody CardDTO dto) {
		return service.getById(id).map( e -> {
			try {
				Card card = convert(dto);
				card.setId(e.getId());
				service.updateCard(card);
				return ResponseEntity.ok(card);
			} catch (CardException exception) {
				return ResponseEntity.badRequest().body(exception.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Card não encontrado na base da dados.", HttpStatus.BAD_REQUEST));
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity delete(@PathVariable("id") int id) {
		return service.getById(id).map( e -> {
			try {
				service.deleteCard(e);
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			} catch (CardException exception) {
				return ResponseEntity.badRequest().body(exception.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Card não encontrado na base da dados.", HttpStatus.BAD_REQUEST));
	}
	
	private Card convert(CardDTO dto) {
		Container container = containerService
				.getById(dto.getIdContainer())
				.orElseThrow(() -> new CardException("Container não encontrado."));
		Card card = Card.builder().title(dto.getTitle()).container(container).build();
		
		return card;
	}
}
