package com.av.flooringmastery.dao;

import com.av.flooringmastery.dto.Order;
import com.av.flooringmastery.dto.Product;
import com.av.flooringmastery.dto.Tax;
import com.av.flooringmastery.service.FlooringMasteryException;

import java.util.*;

public interface FlooringMasteryDao {

    Order addOrder(Order order);

    List<Order> getAllOrders();
    Order getOrder(Product product);
    Order removeOrder(Product product);


    LinkedHashMap<String, Order> getOrdersByDate();

    void setOrdersByDate(String dateInput) throws FlooringMasteryBadDataException;

    List<String> listOfOrders(String date) throws FlooringMasteryException;

    // We want to use private methods to load a method that will open a file and load it into a HashMap
    void loadDataIntoHashMaps() throws FlooringMasteryFileException,  FlooringMasteryNoSuchFileException;

    boolean isValidCustomerName(String name);

    boolean isValidProductType(String productName);

    Set<String> getHashMapKeysAsSet(HashMap<String, ?> map);

    Set<String> getStateMapKeysAsSet();

    Product getProduct(String productKey);


    Tax getTax(String stateKey);
    boolean isValidState(String stateName);

    Map<String, Product> getProductMap();
}
