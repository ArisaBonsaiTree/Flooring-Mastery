package com.av.flooringmastery.service;

import com.av.flooringmastery.dao.FlooringMasteryDao;
import com.av.flooringmastery.dao.FlooringMasteryFileException;
import com.av.flooringmastery.dto.Order;
import com.av.flooringmastery.dto.Product;
import com.av.flooringmastery.dto.Tax;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer {
    private static final String ORDER_HEADER = "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total";
    private static final String ORDER_DIR = "Orders";
    private static final String DATA_DIR = "Data";
    private static final String ORDER_NUMBER_TEXT_FILE = "OrderNumber.txt";
    private static final int MINIMUM_AREA = 100;

    private String pattern = "MMddyyyy";

    FlooringMasteryDao dao;

    public FlooringMasteryServiceLayerImpl(FlooringMasteryDao dao) {
        this.dao = dao;
    }

    @Override
    public List<String> displayOrders(String dateInput) throws FlooringMasteryException{
        // Validate the date first
        if(!isDateValid(dateInput)) throw new FlooringMasteryException("Date inputted doesn't follow " + pattern + " format");

        // Check to see if we even get anything
        if(dao.listOfOrders(dateInput) == null){
            throw new FlooringMasteryException(dateInput + " is invalid");
        }
        return dao.listOfOrders(dateInput);
    }

    @Override
    public Order createOrderObject(Order order) throws FlooringMasteryException {
        if(!isValidCustomerName(order.getCustomerName())) throw new FlooringMasteryException("Customer name is invalid. No empty spaces " +
                "or special characters");

        if(!isValidState(order.getState())){
            throw new FlooringMasteryException("We don't do business in " + order.getState());
        }

        if(!isValidProductName(order.getProductType())){
            throw new FlooringMasteryException("Please input a product we offer " + order.getProductType());
        }

        if(!isValidArea(order.getArea())){
            throw new FlooringMasteryException("Please enter a positive value and minimum order is 100 sq ft");
        }

        Product product = dao.getProduct(order.getProductType());
        Tax tax = dao.getTax(order.getState());

        return createOrderObjectWithCost(order.getCustomerName(), product, tax, order.getArea());
    }

    @Override
    public void placeOrder(Order newOrder) throws FlooringMasteryException {
        String orderDetails = String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                newOrder.getOrderNumber(),
                newOrder.getCustomerName(),
                newOrder.getState(),
                newOrder.getTaxRate(),
                newOrder.getProductType(),
                newOrder.getArea(),
                newOrder.getCostPerSquareFoot(),
                newOrder.getLaborCostPerSquareFoot(),
                newOrder.getMaterialCost(),
                newOrder.getLaborCost(),
                newOrder.getTax(),
                newOrder.getTotal()
        );

        appendOrderToFile(newOrder.getOrderDate(), orderDetails, newOrder.getOrderNumber());
    }

    private void appendOrderToFile(String date, String orderDetails, int orderNumber) throws FlooringMasteryException {
        String fileName = String.format("%s/Orders_%s%s", ORDER_DIR, date, ".txt");
        File orderFile = new File(fileName);
        boolean fileExist = orderFile.exists();

        try (FileWriter fileWriter = new FileWriter(orderFile, true)) {
            // If the order file doesn't exist, create it
            if (!fileExist) {
                fileWriter.write(ORDER_HEADER);
                fileWriter.write(System.lineSeparator());
            }

            fileWriter.write(orderDetails);
            fileWriter.write(System.lineSeparator());
            fileWriter.close();

            incrementOrderNumber(orderNumber);

        } catch (FlooringMasteryException e) {
            throw new FlooringMasteryException(e.getMessage());
        }catch (IOException e){
            throw new FlooringMasteryException(e.getMessage());
        }
    }

    private void incrementOrderNumber(int count) throws FlooringMasteryException {
        count++;

        try {
            BufferedWriter incrementWriter = new BufferedWriter(new FileWriter(DATA_DIR + "/" + ORDER_NUMBER_TEXT_FILE));
            incrementWriter.write(Integer.toString(count));
            incrementWriter.close();
        } catch (Exception e) {
            throw new FlooringMasteryException("Order number file can't be found");
        }
    }

    private Order createOrderObjectWithCost(String customerName, Product product, Tax tax, BigDecimal area) throws FlooringMasteryException {
        int orderNumber = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(DATA_DIR + "/" + ORDER_NUMBER_TEXT_FILE));
            String lineCount = reader.readLine();

            if (lineCount != null) {
                orderNumber = Integer.parseInt(lineCount);
            }
        } catch (Exception e) {
            throw new FlooringMasteryException("No such file exist");
        }

        return new Order(orderNumber, customerName, tax.getStateAbbreviation(), tax.getTaxRate(), product.getProductType(), area, product.getCostPerSquareFoot(), product.getLaborCostPerSquareFoot());
    }

    private boolean isDateValid(String dateString) throws FlooringMasteryException{
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        // Set lenient to false, so it will ONLY accept dates in the correct format
        dateFormat.setLenient(false);

        try{
            Date date = dateFormat.parse(dateString);

            if(dateFormat.format(date).equals(dateString)){
                return true;
            }

        } catch (ParseException e) {
            return false;
        }
        return false;
    }

    private boolean isValidCustomerName(String customerName){
        if (customerName == null || customerName.isEmpty()) {
            return false;
        }
        String regex = "^[a-zA-Z0-9.,\\s]+$";

        return customerName.matches(regex);
    }

    private boolean isValidState(String pickedState){
        pickedState.toUpperCase();
        Set<String> states = dao.getStateMapKeysAsSet();
        return states.contains(pickedState);
    }

    private boolean isValidProductName(String productName){
        productName = productName.substring(0, 1).toUpperCase() + productName.substring(1).toLowerCase();
        return dao.getProductMap().containsKey(productName);
    }

    public Set<String> getStates() {
        Set<String> stateMapKeys = dao.getTaxMap().keySet();
        String[] keyArray = stateMapKeys.toArray(new String[stateMapKeys.size()]);
        return new HashSet(Arrays.asList(keyArray));
    }

    @Override
    public void loadDataIntoHashMaps() throws FlooringMasteryException {
        dao.loadDataIntoHashMaps();
    }

    public Map<String, Product> getProducts() {
        return dao.getProductMap();
    }

    private boolean isValidArea(BigDecimal area){
        BigDecimal threshold = new BigDecimal(MINIMUM_AREA);
        return area.compareTo(threshold) >= 0;
    }
}
