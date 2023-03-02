package com.av.flooringmastery.dao;

import com.av.flooringmastery.dto.Order;
import com.av.flooringmastery.dto.Product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlooringMasteryDaoImpl implements FlooringMasteryDao{
    public static final String DELIMITER = ",";

    private Map<String, Product> productMap = new HashMap<>();

    private List<Product> products = new ArrayList<>();

    // Keep it simple and just put all the orders here
    private Map<Integer, Order> orderMap = new HashMap<>();


    @Override
    public Order addOrder(Order order) {
        Order newOrder = orderMap.put(order.getOrderNumber(), order);
        return newOrder;
    }

    @Override
    public List<Order> getAllOrders() {
        return new ArrayList(orderMap.values());
    }

    @Override
    public Order getOrder(Product product) {
        return null;
    }

    @Override
    public Order removeOrder(Product product) {
        return null;
    }

    // TODO: Change exception to custom exception
    public void loadProductsFromFile() throws IOException {
        String filePath = Paths.get("Data", "Products").toString();

        List<Product> products = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(DELIMITER);
                String productType = values[0];
                BigDecimal costPerSquareFoot = new BigDecimal(values[1]);
                BigDecimal laborCostPerSquareFoot = new BigDecimal(values[2]);

                Product product = new Product(productType, costPerSquareFoot, laborCostPerSquareFoot);
                products.add(product);
            }
            // TODO: NEED TO MAKE A CUSTOM EXCEPTION
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getProductArray(){
        return new ArrayList(products);
    }

}
