package demo.promotionservice.service;

import demo.promotionservice.dto.ProductEvent;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    private static final String CHANNEL_NAME = "promotion-updates";
    public void updatePromotionPrice(Long productId, Double newPrice){
        ProductEvent event = new ProductEvent(productId,newPrice, Instant.now());

        try {
            String jsonPayload = objectMapper.writeValueAsString(event);

            stringRedisTemplate.convertAndSend(CHANNEL_NAME,jsonPayload);

            System.out.println("[Promotion-Service] -> Published Event: " + jsonPayload);
        } catch (Exception e) {
            System.err.println("[Promotion-Service] Lỗi khi serialize hoặc publish message: " + e.getMessage());
        }
    }

}
