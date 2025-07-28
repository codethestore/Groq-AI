package com.springboot.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.project.config.CustomException;
import com.springboot.project.service.ProfAIService;

@RestController
public class profAiController {

	@Autowired
	ProfAIService profAIService;

	@PostMapping(value = "/generate/prompt")
	public ResponseEntity<String> generatePrompt(@RequestBody String input) throws CustomException {
		return new ResponseEntity<>(profAIService.getPrompt(input), HttpStatus.OK);
	}
	
	@PostMapping(value = "/generate/response")
	public ResponseEntity<String> generateResponse(@RequestBody String input) throws CustomException {
		return new ResponseEntity<>(profAIService.getResponse(input), HttpStatus.OK);
	}

}
