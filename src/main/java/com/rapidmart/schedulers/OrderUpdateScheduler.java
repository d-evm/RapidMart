package com.rapidmart.schedulers;

import com.rapidmart.dtos.OrderStatusUpdateDTO;
import com.rapidmart.models.Order;
import com.rapidmart.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderUpdateScheduler {

    private final OrderRepository orderRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Scheduled(fixedRate = 30000) // every 30 seconds
    public void sendOrderStatusUpdates() {
        System.out.println("Order scheduler triggered at " + LocalDateTime.now());

        List<Order> activeOrders = orderRepository.findAll()
                .stream()
                .filter(o -> !o.getStatus().equalsIgnoreCase("Delivered"))
                .toList();

        for (Order order : activeOrders) {
            int minsLeft = (int) Duration.between(LocalDateTime.now(), order.getEstimatedDeliveryTime()).toMinutes();

            String status;
            if (minsLeft > 12) status = "Order Confirmed";
            else if (minsLeft > 7) status = "Packed";
            else if (minsLeft > 0) status = "Out for Delivery";
            else status = "Delivered";

            if (!order.getStatus().equals(status)) {
                order.setStatus(status);
                orderRepository.save(order);
            }

            OrderStatusUpdateDTO dto = new OrderStatusUpdateDTO();
            dto.setOrderId(order.getId());
            dto.setStatus(status);
            dto.setMinutesRemaining(Math.max(minsLeft, 0));

            messagingTemplate.convertAndSend("/topic/order-updates/" + order.getId(), dto);
        }
    }
}
