package demo.inventoryservice.service;

import demo.inventoryservice.dto.OrderDetails;
import demo.inventoryservice.dto.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final RedissonClient redissonClient;
    @KafkaListener(
            topics = "order-created-event",
            groupId = "inventory-group"
    )
    public void updateInventory(OrderEvent orderEvent){
        System.out.println("Inventory nhận được 1 thông tin đơn hàng mới: ");
        System.out.println("Mã đơn hàng: " + orderEvent.getOrders().getOrderId());
        System.out.println("Địa chỉ nhận đơn: " + orderEvent.getOrders().getReceivedAddress());
        System.out.println("Khách hàng: " + orderEvent.getOrders().getFullName());
        RLock lock = redissonClient.getLock("lock:product:" + orderEvent.getOrderDetails().getFirst().getProductId());
        try {
            boolean isLocked = lock.tryLock(10,30, TimeUnit.SECONDS);

            if(isLocked){
                System.out.println("Đã lấy được khóa. đang xử lý đặt hàng cho: " + orderEvent.getOrderDetails().getFirst().getProductId());

                Thread.sleep(3000);

                System.out.println("Đặt hàng thành công: " + orderEvent.getOrderDetails().getFirst().getProductId());

                System.out.println("\n\n");

                System.out.println("Cập nhật số lượng trong kho: ");
                for(OrderDetails orderDetails : orderEvent.getOrderDetails()){
                    System.out.println("-------------------------------");
                    System.out.println("Mã sản phẩm: " + orderDetails.getOrderId());
                    System.out.println("Số lượng: " + orderDetails.getQuantity());
                }
            }else {
                System.out.println("Hệ thống bận, vui lòng thử lại sau. Đơn hàng " +orderEvent.getOrderDetails().getFirst().getProductId() + " đang được xử lý");
            }
        }catch (InterruptedException ex){
            Thread.currentThread().interrupt();
        }finally {
            if (lock.isHeldByCurrentThread()){
                lock.unlock();
                System.out.println("đã nhả khóa cho đơn hàng: " +orderEvent.getOrderDetails().getFirst().getProductId());
            }
        }

    }
}
