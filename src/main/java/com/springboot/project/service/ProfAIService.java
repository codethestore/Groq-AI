package com.springboot.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.springboot.project.config.CustomException;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProfAIService {

	@Value("${groq.url}")
	private String url;

	@Value("${groq.clientSecret}")
	private String clientSecret;

	@Autowired
	private WebClient webclient;

	private static final String template = "generate an efficeient prompt to request any AI chat assist to: ";

	public String getPrompt(String input) throws CustomException {
		if (StringUtils.isBlank(input)) {
			return "Haha nice joke..";
		}
		JsonNode request = getRequest(input, true);
		return callGroqAPI(input, request);
	}

	public String getResponse(String input) throws CustomException {
		if (StringUtils.isBlank(input)) {
			return "Haha nice joke..";
		}
		JsonNode request = getRequest(input, false);
		return callGroqAPI(input, request);
	}

	private String callGroqAPI(String input, JsonNode request) throws CustomException {
		try {
			log.info("Calling the groq API: {} for the request: {}", url, input);
			JsonNode response = webclient.post().uri(url).bodyValue(request).headers(headers -> {
				headers.setBearerAuth(clientSecret);
			}).retrieve().bodyToMono(JsonNode.class).block();
			if (response.has("choices") && !response.get("choices").isEmpty()
					&& response.get("choices").get(0).has("message")
					&& response.get("choices").get(0).get("message").has("content")) {
				return response.get("choices").get(0).get("message").get("content").asText();
			} else {
				return "Oops something didn't work..";
			}
		} catch (HttpClientErrorException e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (HttpServerErrorException e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private JsonNode getRequest(String input, boolean isPrompt) {
		if (isPrompt) {
			input = template + input;
		}
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestObject = mapper.createObjectNode();
		requestObject.put("model", "llama-3.3-70b-versatile");
		ArrayNode messages = mapper.createArrayNode();
		ObjectNode message = mapper.createObjectNode();
		message.put("role", "user");
		message.put("content", input);
		messages.add(message);
		requestObject.set("messages", messages);
		return requestObject;
	}

}
