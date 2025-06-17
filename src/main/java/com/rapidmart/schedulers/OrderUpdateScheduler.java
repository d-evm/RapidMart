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
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime eta = order.getEstimatedDeliveryTime();
            LocalDateTime orderTime = order.getOrderTime();

            long totalDurationSec = Duration.between(orderTime, eta).getSeconds();
            long elapsedSec = Duration.between(orderTime, now).getSeconds();

            double progress = (double) elapsedSec / totalDurationSec;

            String status;
            if (progress < 0.1) status = "Order Confirmed";
            else if (progress < 0.3) status = "Packing";
            else if (progress < 1.0) status = "Out for Delivery";
            else status = "Delivered";

            if (!order.getStatus().equals(status)) {
                order.setStatus(status);
                orderRepository.save(order);
            }

            int minsLeft = (int) Duration.between(now, eta).toMinutes();

            OrderStatusUpdateDTO dto = new OrderStatusUpdateDTO();
            dto.setOrderId(order.getId());
            dto.setStatus(status);
            dto.setMinutesRemaining(Math.max(minsLeft, 0));

            messagingTemplate.convertAndSend("/topic/order-updates/" + order.getId(), dto);
        }
    }
}

