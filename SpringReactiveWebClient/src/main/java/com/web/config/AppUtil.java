package com.web.config;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AppUtil {
	
	public WebClient.Builder getWebClient()
	{
		return WebClient.builder();
	}
	
}