package demo.promotionservice.controller;

import demo.promotionservice.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;
    @PostMapping("/update-price")
    public ResponseEntity<String> updatePrice(@RequestParam Long productId, @RequestParam Double newPrice){
        promotionService.updatePromotionPrice(productId,newPrice);
        return ResponseEntity.ok("Đã cập nhật giá khuyến mãi và phát thông báo tới Redis Channel!");
    }

}
