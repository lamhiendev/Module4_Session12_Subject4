package demo.productservice.config;

import demo.productservice.listener.PromotionReceiver;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;



@Configuration
@EnableCaching
public class RedisConfig {
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter messageListenerAdapter){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListenerAdapter, new PatternTopic("promotion-updates"));
        return container;
    }
    @Bean
    MessageListenerAdapter listenerAdapter(PromotionReceiver receiver){
        return new MessageListenerAdapter(receiver,"updatePromotion");
    }
}
