package com.av.flooringmastery.service;

import com.av.flooringmastery.dto.Order;

import java.util.List;
import java.util.Set;

public interface FlooringMasteryServiceLayer {

    List<String> displayOrders(String s) throws FlooringMasteryException;

    Order createOrderObject(Order order) throws FlooringMasteryException;

    Set<String> getStates();

    void loadDataIntoHashMaps() throws FlooringMasteryException;

    void placeOrder(Order newOrder) throws FlooringMasteryException;
}
