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

    Order deleteOrder(String dateInput, String orderNumber) throws FlooringMasteryException;

    void deleteOrder(Order deletedOrder, String dateInput) throws FlooringMasteryException;

    Order editOrder(String dateInput, String orderNumber) throws FlooringMasteryException;

    Order compareAndEdit(Order editOrder, Order newOrderData) throws FlooringMasteryException;

    Order computeNewCost(Order orderToBeEdited);

    void editMapAndOverride(Order editOrder, String dateInput) throws FlooringMasteryException;

    void backupData() throws FlooringMasteryException;
}
