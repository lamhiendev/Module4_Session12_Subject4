package demo.promotionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductEvent {
    private Long productId;
    private Double newPrice;
    private Instant timeStamp;
}
