package com.av.flooringmastery.dao;

import com.av.flooringmastery.dto.Order;
import com.av.flooringmastery.dto.Product;
import com.av.flooringmastery.dto.Tax;
import com.av.flooringmastery.service.FlooringMasteryException;

import java.util.*;

public interface FlooringMasteryDao {

    Order addOrder(Order order);

    List<Order> getAllOrders();

    LinkedHashMap<String, Order> getOrdersByDate();

    List<String> listOfOrders(String date) throws FlooringMasteryException;

    void loadDataIntoHashMaps() throws FlooringMasteryException;

    Set<String> getHashMapKeysAsSet(HashMap<String, ?> map);

    Set<String> getStateMapKeysAsSet();

    Product getProduct(String productKey);

    Tax getTax(String stateKey);

    Map<String, Product> getProductMap();

    List<Product> getProducts();

    void setProducts(List<Product> products);

    Map<Integer, Order> getOrderMap();

    void setOrderMap(Map<Integer, Order> orderMap);

    HashMap<String, Tax> getTaxMap();

    void setTaxMap(HashMap<String, Tax> taxMap);

    void setProductMap(Map<String, Product> productMap);

    void setOrdersByDate(LinkedHashMap<String, Order> ordersByDate);
}
