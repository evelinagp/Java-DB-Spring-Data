package com.example.gamestore.services;


import com.example.gamestore.domain.entities.Order;
import com.example.gamestore.domain.models.OrderCartModel;
import com.example.gamestore.repositories.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private ModelMapper modelMapper;
    private OrderRepository orderRepository;

    public OrderService(ModelMapper modelMapper, OrderRepository orderRepository) {
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
    }

    public void buyGames(OrderCartModel orderCartModel) {
        Order order = this.modelMapper.map(orderCartModel, Order.class);

        this.orderRepository.save(order);
    }
}