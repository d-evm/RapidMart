package com.rapidmart.services;

import com.rapidmart.dtos.OrderRequestDTO;
import com.rapidmart.dtos.OrderResponseDTO;
import com.rapidmart.models.*;
import com.rapidmart.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ZoneRepository zoneRepository;
    private final UserRepository userRespository;

    public OrderResponseDTO placeOrder(OrderRequestDTO request){

        // get the current user from security context
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRespository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // find eligible zone
        Zone zone = zoneRepository.findByPincode(user.getPincode());
//                .orElseThrow(() -> new RuntimeException("Zone not found for user pincode"));

        Store selectedStore = null;
        double minDistance = Double.MAX_VALUE;
        Map<Long, Product> matchedProducts = new HashMap<>();

        // select nearest store with complete stock
        for (Store store : zone.getStores()){
            System.out.println("Checking in store: " + store.getId() + store.getName());
            boolean canFulfill = true;
            matchedProducts.clear();

            for (OrderRequestDTO.OrderItemRequest item : request.getItems()) {
                System.out.println("Item: " + item.toString());
                Product product = productRepository.findById(item.getProductId()).orElse(null);

                if (product == null) {
                    System.out.println("Product ID " + item.getProductId() + " not found in DB.");
                    canFulfill = false;
                    break;
                }

                if (!product.getStore().getId().equals(store.getId())) {
                    System.out.println("Product ID " + item.getProductId() + " does not belong to Store: " + store.getName());
                    canFulfill = false;
                    break;
                }

                if (product.getQuantityInStock() < item.getQuantity()) {
                    System.out.println("Insufficient stock for product ID " + item.getProductId() + " in store " + store.getName());
                    canFulfill = false;
                    break;
                }

                matchedProducts.put(item.getProductId(), product);
            }

            if (canFulfill){
                // simulate distance
//                System.out.println("Order fulfilled from store: " + store.toString());
                double distance = Math.abs(Double.parseDouble(user.getPincode()) - Double.parseDouble(store.getZone().getPincode())) * 0.5;
                if (distance < minDistance) {
                    selectedStore = store;
                    minDistance = distance;
                }
                break;
            }
        }

        System.out.println("Zone:" + zone.getPincode());

        if (selectedStore == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No store in zone has required stock for all products."
            );
        }

        // deduct from inventory
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderRequestDTO.OrderItemRequest item : request.getItems()) {
            Product product = matchedProducts.get(item.getProductId());
            product.setQuantityInStock(product.getQuantityInStock() - item.getQuantity());
            productRepository.save(product);

            orderItems.add(OrderItem.builder()
                    .product(product)
                    .quantity(item.getQuantity())
                    .build());
        }

        // calculate ETA

        int itemProcessingTimeSec = request.getItems().size() * 20; // 20 sec per item
        int packingTimeSec = 60; // fixed 1 min packing

        // realistic delivery time: base time + per km time
        int baseDeliveryTimeSec = 5 * 60; // minimum 5 minutes
        double perKmTimeSec = 2 * 60; // assume 2 minutes per km
        double deliveryTimeSec = baseDeliveryTimeSec + (minDistance * perKmTimeSec);

        // simulate delay due to pending orders at the store
        int pendingOrders = (int) selectedStore.getOrders().stream()
                .filter(o -> !o.getStatus().equalsIgnoreCase("Delivered"))
                .count();
        int bufferTimeSec = pendingOrders * 60; // 60 sec per order

        // total ETA in seconds
        int totalEtaSeconds = itemProcessingTimeSec + packingTimeSec + (int) deliveryTimeSec + bufferTimeSec;
        LocalDateTime eta = LocalDateTime.now().plusSeconds(totalEtaSeconds);


        // creating order
        Order order = Order.builder()
                .user(user)
                .store(selectedStore)
                .status("Pending")
                .orderTime(LocalDateTime.now())
                .orderItems(new ArrayList<>())
                .estimatedDeliveryTime(eta)
                .build();
        order = orderRepository.save(order);

        for (OrderItem item : orderItems) {
            item.setOrder(order);
        }
        order.setOrderItems(orderItems);
        order = orderRepository.save(order);

        // build response
        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrderId(order.getId());
        response.setStatus(order.getStatus());
        response.setOrderTime(order.getOrderTime());
        response.setStoreName(selectedStore.getName());
        response.setDeliveryPincode(user.getPincode());
        response.setEstimatedDeliveryTime("ETA: " + totalEtaSeconds / 60 + " min");

        List<OrderResponseDTO.OrderItemDTO> itemDTOs = new ArrayList<>();
        for (OrderItem item : order.getOrderItems()) {
            OrderResponseDTO.OrderItemDTO dto = new OrderResponseDTO.OrderItemDTO();
            dto.setProductName(item.getProduct().getName());
            dto.setPrice(item.getProduct().getPrice());
            dto.setQuantity(item.getQuantity());
            itemDTOs.add(dto);
        }
        response.setItems(itemDTOs);

        return response;
    }

}
