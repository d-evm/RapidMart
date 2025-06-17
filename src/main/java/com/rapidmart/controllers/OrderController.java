package com.rapidmart.controllers;

import com.rapidmart.dtos.OrderRequestDTO;
import com.rapidmart.dtos.OrderResponseDTO;
import com.rapidmart.models.Order;
import com.rapidmart.repositories.OrderRepository;
import com.rapidmart.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> placeOrder(@RequestBody OrderRequestDTO request) {
        OrderResponseDTO response = orderService.placeOrder(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderDetails(@PathVariable Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setOrderTime(order.getOrderTime());
        dto.setEstimatedDeliveryTime(order.getEstimatedDeliveryTime().toString());
        return ResponseEntity.ok(dto);
    }

}
