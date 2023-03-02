package com.av.flooringmastery.dao;

import com.av.flooringmastery.dto.Order;
import com.av.flooringmastery.dto.Product;

import java.util.List;

public interface FlooringMasteryDao {

    Order addOrder(Order order);

    List<Order> getAllOrders();
    Order getOrder(Product product);
    Order removeOrder(Product product);
}
