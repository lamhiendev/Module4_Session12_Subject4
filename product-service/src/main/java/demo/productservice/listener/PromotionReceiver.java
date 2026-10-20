package demo.productservice.listener;

import demo.productservice.dto.ProductEvent;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;


@RequiredArgsConstructor
@Component
public class PromotionReceiver {
    private final CacheManager cacheManager;

    private final ObjectMapper objectMapper;

    // Phương thức này sẽ được MessageListenerAdapter gọi tự động
    public void updatePromotion(String message) {
        try {
            System.out.println("[PromotionReceiver] Received raw message: " + message);

            // 1. Parse JSON payload sang DTO
            ProductEvent event = objectMapper.readValue(message, ProductEvent.class);

            // 2. Dùng CacheManager để xóa cache của sản phẩm
            if (cacheManager.getCache("products") != null) {
                cacheManager.getCache("products").evict(event.getProductId());
                System.out.println("[PromotionReceiver] Evicted cache 'products' for productId: " + event.getProductId());
            }

        } catch (Exception e) {
            System.err.println("[PromotionReceiver] Error processing message: " + e.getMessage());
        }
    }
}
