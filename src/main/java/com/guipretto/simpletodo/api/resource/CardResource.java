package com.guipretto.simpletodo.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guipretto.simpletodo.api.dto.CardDTO;
import com.guipretto.simpletodo.exception.CardException;
import com.guipretto.simpletodo.model.entity.Card;
import com.guipretto.simpletodo.model.entity.Container;
import com.guipretto.simpletodo.service.CardService;
import com.guipretto.simpletodo.service.ContainerService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
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
	
	@GetMapping("/getCards/{id}")
	public ResponseEntity getCards(@PathVariable("id") int id) {
		Optional<Container> container = containerService.getById(id);
		if(!container.isPresent()) {
			return ResponseEntity.badRequest().body("Não existe Container com o ID enviado.");
		}
		
		List<Card> cards = service.getAllWithContainerId(container.get());
		return ResponseEntity.ok(cards);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity get( @PathVariable("id") int id ) {
		return service.getById(id)
					.map( card -> new ResponseEntity(card, HttpStatus.OK))
					.orElseGet( () -> new ResponseEntity("Card não encontrado na base da dados.",HttpStatus.NOT_FOUND));
	}
	
	private Card convert(CardDTO dto) {
		Container container = containerService
				.getById(dto.getIdContainer())
				.orElseThrow(() -> new CardException("Container não encontrado."));
		Card card = Card.builder().title(dto.getTitle()).container(container).build();
		
		return card;
	}
}
