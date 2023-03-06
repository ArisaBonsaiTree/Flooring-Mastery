package com.av.flooringmastery.dao;

import com.av.flooringmastery.dto.Order;
import com.av.flooringmastery.dto.Product;
import com.av.flooringmastery.dto.Tax;
import com.av.flooringmastery.service.FlooringMasteryException;

import java.math.BigDecimal;
import java.util.*;

public class FlooringMasteryDaoStubImpl implements FlooringMasteryDao{

    public Order onlyOrder;

    public FlooringMasteryDaoStubImpl(){
        onlyOrder = new Order();
        onlyOrder.setCustomerName("Test");
        onlyOrder.setState("CA");
        onlyOrder.setProductType("Wood");
        onlyOrder.setArea(new BigDecimal("150"));
    }

    public FlooringMasteryDaoStubImpl(Order onlyOrder){this.onlyOrder = onlyOrder; }


    @Override
    public Order addOrder(Order order) {
        return onlyOrder;
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        orderList.add(onlyOrder);
        return orderList;
    }

    @Override
    public List<String> listOfOrders(String date){
        return null;
    }

    @Override
    public LinkedHashMap<String, Order> getOrdersByDate() {
        return null;
    }



    @Override
    public void loadDataIntoHashMaps() throws FlooringMasteryException {

    }

    @Override
    public Set<String> getHashMapKeysAsSet(HashMap<String, ?> map) {
        return null;
    }

    @Override
    public Set<String> getStateMapKeysAsSet() {
        return null;
    }

    @Override
    public Product getProduct(String productKey) {
        return null;
    }

    @Override
    public Tax getTax(String stateKey) {
        return null;
    }

    @Override
    public Map<String, Product> getProductMap() {
        return null;
    }

    @Override
    public List<Product> getProducts() {
        return null;
    }

    @Override
    public void setProducts(List<Product> products) {

    }

    @Override
    public Map<Integer, Order> getOrderMap() {
        return null;
    }

    @Override
    public void setOrderMap(Map<Integer, Order> orderMap) {

    }

    @Override
    public HashMap<String, Tax> getTaxMap() {
        return null;
    }

    @Override
    public void setTaxMap(HashMap<String, Tax> taxMap) {

    }

    @Override
    public void setProductMap(Map<String, Product> productMap) {

    }

    @Override
    public void setOrdersByDate(LinkedHashMap<String, Order> ordersByDate) {

    }
}
