package com.springboot.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.project.config.CustomException;
import com.springboot.project.service.ProfAIService;
import com.springboot.project.vo.ChatRequestVO;
import com.springboot.project.vo.ChatResponseVO;

@RestController
public class ProfAiController {

	@Autowired
	ProfAIService profAIService;

	@PostMapping(value = "/generate/prompt")
	public ResponseEntity<ChatResponseVO> generatePrompt(@RequestBody ChatRequestVO requestVO) throws CustomException {
		return new ResponseEntity<>(new ChatResponseVO(profAIService.getPrompt(requestVO.getInput())), HttpStatus.OK);
	}

	@PostMapping(value = "/generate/response")
	public ResponseEntity<ChatResponseVO> generateResponse(@RequestBody ChatRequestVO requestVO)
			throws CustomException {
		return new ResponseEntity<>(new ChatResponseVO(profAIService.getResponse(requestVO.getInput())), HttpStatus.OK);
	}

}
