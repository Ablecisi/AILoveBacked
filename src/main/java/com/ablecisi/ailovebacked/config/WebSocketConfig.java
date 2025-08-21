package com.ablecisi.ailovebacked.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * AILoveBacked <br>
 * com.ailianlian.server.config <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/8/16
 * 星期六
 * 23:18
 **/
@Configuration
@EnableWebSocketMessageBroker // 启用WebSocket消息代理
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS(); // 注册STOMP端点
        // 允许跨域访问，使用SockJS作为回退选项
        // 这里的"/ws"是WebSocket连接的端点，可以根据需要修改
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // 启用简单的消息代理，订阅前缀为"/topic"
        registry.setApplicationDestinationPrefixes("/app"); // 设置应用程序目的地前缀，客户端发送消息时需要使用这个前缀
    }
}
