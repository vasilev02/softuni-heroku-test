package com.example.shop.web.api;

import com.example.shop.model.entity.Order;
import com.example.shop.model.service.orderServiceModels.OrderProfileServiceModel;
import com.example.shop.repository.UserRepository;
import com.example.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderRestController {
    private final UserRepository userRepository;
    private  final OrderService orderService;

    @Autowired
    public OrderRestController(UserRepository userRepository, OrderService orderService) {
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    @GetMapping("b/{id}")
    public ResponseEntity<List<OrderProfileServiceModel>> getAllOrder(@PathVariable String id) {
        List<OrderProfileServiceModel> orders = orderService.getAllUserOrdersByUsername(userRepository.findById(id).get().getUsername());

        if (orders.isEmpty()) {
           return new ResponseEntity<>(orders, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
    }
}
