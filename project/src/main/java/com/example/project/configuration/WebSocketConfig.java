package com.example.project.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer
{

	@Override
	public  void configureMessageBroker(MessageBrokerRegistry config)
	{
		config.enableSimpleBroker("/topic");
		config.setApplicationDestinationPrefixes("/app");
	}
	
	@Override
	 public void registerStompEndpoints(StompEndpointRegistry registry) {
	        // Endpoint for clients to connect
	        registry.addEndpoint("/chat")
	                .setAllowedOriginPatterns("https://elearning-platform-frontend-8o0m.onrender.com") //https://elearning-platform-frontend-8o0m.onrender.com/ // https://elearning-platform-frontend-rho.vercel.app
	                .withSockJS(); 
	    }

 //   http://localhost:3000
} 
 