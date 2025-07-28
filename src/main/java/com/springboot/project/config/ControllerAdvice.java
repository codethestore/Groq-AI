package com.springboot.project.config;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

	@ResponseBody
	@ExceptionHandler(CustomException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleCustomException(CustomException e) {
		if (e.getStatus() == HttpStatus.BAD_REQUEST) {
			return Map.of("message", "Oops we messed it up with groq, please try again.", "status",
					HttpStatus.BAD_REQUEST);
		} else {
			return Map.of("message", "Sorry out AI chat-bot is not co-operating/not reachable, please try again.",
					"status", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
