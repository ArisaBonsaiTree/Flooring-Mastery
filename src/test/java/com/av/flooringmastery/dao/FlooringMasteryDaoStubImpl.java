package com.av.flooringmastery.dao;

import com.av.flooringmastery.dto.Order;
import com.av.flooringmastery.dto.Product;
import com.av.flooringmastery.dto.Tax;
import com.av.flooringmastery.service.FlooringMasteryException;

import java.util.*;

public class FlooringMasteryDaoStubImpl implements FlooringMasteryDao{

    public Order onlyOrder;

    public FlooringMasteryDaoStubImpl(){
        onlyOrder = new Order();

    }

    public FlooringMasteryDaoStubImpl(Order onlyOrder){this.onlyOrder = onlyOrder; }


    @Override
    public Order addOrder(Order order) {
        return null;
    }

    @Override
    public Order removeOrder(Product product) {
        return null;
    }


    @Override
    public List<Order> getAllOrders() {
        return null;
    }





    @Override
    public Order getOrder(Product product) {
        return null;
    }


    @Override
    public LinkedHashMap<String, Order> getOrdersByDate() {
        return null;
    }

    @Override
    public List<String> listOfOrders(String date) throws FlooringMasteryException {
        return null;
    }

    @Override
    public void loadDataIntoHashMaps() throws FlooringMasteryException {

    }

    @Override
    public boolean isValidCustomerName(String name) {
        return false;
    }

    @Override
    public boolean isValidProductType(String productName) {
        return false;
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
    public boolean isValidState(String stateName) {
        return false;
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
