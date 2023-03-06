package com.av.flooringmastery.controller;

import com.av.flooringmastery.dao.FlooringMasteryBadDataException;
import com.av.flooringmastery.dao.FlooringMasteryDao;
import com.av.flooringmastery.dao.FlooringMasteryFileException;
import com.av.flooringmastery.dto.Order;
import com.av.flooringmastery.dto.Product;
import com.av.flooringmastery.dto.Tax;
import com.av.flooringmastery.service.FlooringMasteryException;
import com.av.flooringmastery.service.FlooringMasteryServiceLayerImpl;
import com.av.flooringmastery.ui.FlooringMasteryView;
import com.av.flooringmastery.ui.UserIO;
import com.av.flooringmastery.ui.UserIOConsoleImpl;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class FlooringMasteryController {
    private static final String FILENAME = "Data/OrderNumber.txt";

    private FlooringMasteryView view;
    private FlooringMasteryServiceLayerImpl service;
    private FlooringMasteryDao dao;


    private UserIO io = new UserIOConsoleImpl();

    public FlooringMasteryController(FlooringMasteryView view, FlooringMasteryServiceLayerImpl service) {
        this.view = view;
        this.service = service;
    }



    public void run() {
        int userChoice;

        outer:
        while (true) {
            userChoice = getUserSelection();

            try {
                switch (userChoice) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        createOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        deleteAnOrder();
                        break;
                    case 5:
                        io.print("Exporting All Data");
                        break;
                    case 6:
                        io.print("Quiting");
                        break outer;
                    default:
                        io.print("Unknown command");
                }
            } catch (Exception e) {
                view.displayErrorMessage(e.getMessage());
            }
        }
        exitMessage();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }

    private void displayOrders() {
        view.displayDisplayAllBanner();
        boolean hasErrors;
        view.informUserToQuit();
        do {
            try {
                String dateInput = view.getOrderDate();
                if(dateInput.equals("q")) break; // Break out of the loop EARLY!
                List<String> listOfOrders = service.displayOrders(dateInput);
                view.displayAllOrderList(listOfOrders);
                hasErrors = false;
            }
            catch (FlooringMasteryException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        }while (hasErrors);
    }

    // TODO: Dafuq you didnt even finish this bro??????
    private void deleteAnOrder() throws FlooringMasteryBadDataException {
        String dateInput = null;
        String orderNumber = null;
        try{
            // TODO: Should validate the input before we even start
            dateInput = view.getOrderDate();
            orderNumber = view.getOrderNumber();

            dao.setOrdersByDate(dateInput);

            LinkedHashMap<String, Order> orderMap = dao.getOrdersByDate();
            Order deleteOrder = orderMap.get(orderNumber);

            view.printOrderSummary(deleteOrder);
            String userConfirmation = io.readString("If you want to place DELETE the order, type 'y', else any other input will NOT delete the order");

            if (userConfirmation.equalsIgnoreCase("y")) {
                deleteOrder.setOrderNumber(deleteOrder.getOrderNumber() * -1); // Made it negative so it's gone!
                orderMap.remove(orderNumber); // DELETE IT FROM THE HASHMAP
                deleteOrder(orderMap, dateInput);
                orderDeletedMessage();
                return;
            }

            orderNotDeletedMessage();
        }catch (Exception e){
            throw new FlooringMasteryBadDataException("No order file with " + dateInput + " or with order number " + orderNumber.toString());
        }
    }

    private void orderNotDeletedMessage() {
        view.displayOrderNotDeletedBanner();
    }

    private void orderDeletedMessage() {
        view.displayOrderDeletedBanner();
    }

    // TODO: HOLD UP !!!!!!
    private void editOrder() throws FlooringMasteryFileException {
        String dateInput = null;
        String orderNumber = null;
        Order editOrder;
        try {
            dao.loadDataIntoHashMaps();
            dateInput = view.getOrderDate();
            orderNumber = view.getOrderNumber();

            dao.setOrdersByDate(dateInput);

            LinkedHashMap<String, Order> orderMap = dao.getOrdersByDate();
            editOrder = orderMap.get(orderNumber);

            io.print("Just type nothing and press 'Enter' if you don't want to edit the value");
            io.print("Customer Name: " + editOrder.getCustomerName());

            String customerName = io.readString("Enter a new customer name").trim();
            if (!customerName.isEmpty()) { // If it's NOT EMPTY
                // It's not empty, so we should validate it
                if (!dao.isValidCustomerName(customerName)){
                    throw new FlooringMasteryBadDataException("Name is limited to characters [a-zA-Z][0-9], periods, and comma");
                }


                editOrder.setCustomerName(customerName);
            }

            // TODO: Perhaps list all choices?
            io.print("Current State: " + editOrder.getState());

            Set<String> states = dao.getStateMapKeysAsSet();
            io.printF("States we do business with: [%s]\n", io.printHashSet(states));

            String state = io.readString("Enter a new state").trim();
            if (!state.isEmpty()) {
                if (!dao.isValidState(state)){
                    throw new FlooringMasteryBadDataException("We don't do business with " + state);
                }
                editOrder.setState(state);
            }


            // TODO: Perhaps list all the options
            io.print("Product type: " + editOrder.getProductType());

            Map<String, Product> listOfProducts = dao.getProductMap();
            view.displayAllProducts(listOfProducts);

            String productType = io.readString("Enter a new product type").trim();
            if (!productType.isEmpty()) {
                if (!dao.isValidProductType(productType)){
                    throw new FlooringMasteryBadDataException("No such product named " + productType.substring(0, 1).toUpperCase() + productType.substring(1));
                }
                editOrder.setProductType(productType);
            }

            io.print("Area: " + editOrder.getArea());
            String area = io.readString("Enter a new area").trim();
            if (!area.isEmpty()) {
                if (area.compareTo(String.valueOf(BigDecimal.ZERO)) < 0 || area.compareTo(String.valueOf(new BigDecimal("100"))) < 0)
                    throw new FlooringMasteryBadDataException("Please enter a positive value and minimum order is 100 sq ft.");
                editOrder.setArea(new BigDecimal(area));
            }

            // TODO: WE NEED TO VERIFY AND PRINT OPTIONS!!!!

            Product product = dao.getProduct(editOrder.getProductType().substring(0, 1).toUpperCase() + editOrder.getProductType().substring(1));

            Tax tax = dao.getTax(editOrder.getState().toUpperCase().replaceAll("\\s", ""));
            Order newEdit = editNewCost(editOrder.getCustomerName(), product, tax, editOrder.getArea(), editOrder.getOrderNumber());




            // Display Order before we touch it
            view.printOrderSummary(newEdit);

            String userConfirmation = io.readString("If you want to edit the order, type 'y', else any other input will cancel the order");

            if (userConfirmation.equalsIgnoreCase("y")) {

                // TODO: EDIT THE ORDER
                orderMap.put(orderNumber, editOrder);
                editOrder(orderMap, dateInput);
//                placeOrder(order);

                orderEditedMessage();
                return;
            }
            orderNotEditedMessage();


        }catch (Exception e){
            throw new FlooringMasteryFileException("Bad");
        }

    }

    private void editOrder(LinkedHashMap<String, Order> orderMap, String fileNameDate) throws FlooringMasteryBadDataException {
        String order_dir = "Orders";
        String header = "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total";
        String fileName = String.format("%s/Orders_%s%s", order_dir, fileNameDate, ".txt");
        File orderFile = new File(fileName);

        try(FileWriter fileWriter = new FileWriter(orderFile)){
            fileWriter.write(header);
            fileWriter.write(System.lineSeparator());

            for(Order order: orderMap.values()){
                String orderDetails = String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                        order.getOrderNumber(),
                        order.getCustomerName(),
                        order.getState(),
                        order.getTaxRate(),
                        order.getProductType(),
                        order.getArea(),
                        order.getCostPerSquareFoot(),
                        order.getLaborCostPerSquareFoot(),
                        order.getMaterialCost(),
                        order.getLaborCost(),
                        order.getTax(),
                        order.getTotal()
                );

                fileWriter.write(orderDetails);
                fileWriter.write(System.lineSeparator());
            }

        } catch (IOException e) {
            throw new FlooringMasteryBadDataException("Problem editing the data");
        }
    }

    private void orderNotEditedMessage() {
        view.displayOrderNotEditedBanner();
    }

    private void orderEditedMessage() {
        view.displayOrderEditedBanner();
    }

    private void orderPlacedMessage() {
        view.displayOrderPlacedBanner();
    }

    private void orderNotPlacedBanner() {
        view.displayOrderNotPlacedBanner();
    }

    private int getUserSelection() {
        return view.printOptionsAndGetSelection();
    }


    private void createOrder() {
        view.displayCreateOrderBanner();
        view.informUserToQuit();
        boolean hasErrors;
        do {
            try {
                service.loadDataIntoHashMaps();
                Set<String> statesWeDoService = service.getStates();
                Map<String, Product> productsWeOffer = service.getProducts();

                Order currentObject = view.getNewOrderInfo(statesWeDoService, productsWeOffer);

                Order newOrder = service.createOrderObject(currentObject);
                view.printOrderSummary(newOrder);

                String getUserConfirmation = view.createOrderUserConfirmation();

                if(getUserConfirmation.equalsIgnoreCase("y")){
                    service.placeOrder(newOrder);
                    orderPlacedMessage();
                }

                else{
                    orderNotPlacedMessage();
                }
                hasErrors = false;
            }

            catch (FlooringMasteryException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
                if(view.userWantsToLeave()) break;
            }
        } while (hasErrors);
    }

    private void orderNotPlacedMessage() {
        view.displayOrderNotPlacedBanner();
    }

    private Order editNewCost(String customerName, Product product, Tax tax, BigDecimal area, Integer orderNumber) throws FlooringMasteryFileException {
        return new Order(orderNumber, customerName, tax.getStateAbbreviation(), tax.getTaxRate(), product.getProductType(), area, product.getCostPerSquareFoot(), product.getLaborCostPerSquareFoot());
    }

    private void deleteOrder(LinkedHashMap<String, Order> orderMap, String orderDate) throws IOException {
        String order_dir = "Orders";
        String header = "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total";
        String fileName = String.format("%s/Orders_%s%s", order_dir, orderDate, ".txt");
        File orderFile = new File(fileName);

        FileWriter writer = new FileWriter(orderFile);

        writer.write(header + "\n");

        for (Order order : orderMap.values()) {
            StringBuilder sb = new StringBuilder();
            sb.append(order.getOrderNumber()).append(",");
            sb.append(order.getCustomerName()).append(",");
            sb.append(order.getState()).append(",");
            sb.append(order.getTaxRate()).append(",");
            sb.append(order.getProductType()).append(",");
            sb.append(order.getArea()).append(",");
            sb.append(order.getCostPerSquareFoot()).append(",");
            sb.append(order.getLaborCostPerSquareFoot()).append(",");
            sb.append(order.getMaterialCost()).append(",");
            sb.append(order.getLaborCost()).append(",");
            sb.append(order.getTax()).append(",");
            sb.append(order.getTotal()).append("\n");

            // write the order to the file
            writer.write(sb.toString());
        }

        writer.close();

    }


}
