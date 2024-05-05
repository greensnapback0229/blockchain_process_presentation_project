package media_blockchain.blockchain.stomp;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class StompConfigure implements WebSocketMessageBrokerConfigurer {
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry){
			registry
				.addEndpoint("/sock/conn")
				.setAllowedOrigins("*");
			//버전 낮은 브라우저 지원
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config){
		config.enableSimpleBroker("/topic");	//구독할 경로
		config.setApplicationDestinationPrefixes("/app");	//메세지 보낼 경로
	}
}
