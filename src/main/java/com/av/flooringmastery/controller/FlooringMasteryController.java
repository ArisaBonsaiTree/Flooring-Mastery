package com.av.flooringmastery.controller;

import com.av.flooringmastery.dao.FlooringMasteryBadDataException;
import com.av.flooringmastery.dao.FlooringMasteryDao;
import com.av.flooringmastery.dao.FlooringMasteryFileException;
import com.av.flooringmastery.dao.FlooringMasteryNoSuchFileException;
import com.av.flooringmastery.dto.Order;
import com.av.flooringmastery.dto.Product;
import com.av.flooringmastery.dto.Tax;
import com.av.flooringmastery.service.FlooringMasteryServiceLayer;
import com.av.flooringmastery.ui.FlooringMasteryView;
import com.av.flooringmastery.ui.UserIO;
import com.av.flooringmastery.ui.UserIOConsoleImpl;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class FlooringMasteryController {
    private static final String FILENAME = "Data/OrderNumber.txt";

    private FlooringMasteryView view;
    private FlooringMasteryServiceLayer service;
    private FlooringMasteryDao dao;


    private UserIO io = new UserIOConsoleImpl();

    public FlooringMasteryController(FlooringMasteryView view, FlooringMasteryDao dao) {
        this.view = view;
        this.dao = dao;
    }

    public void run() {
        int userChoice;

        outer:
        while (true) {
            userChoice = getUserSelection();

            try{
                switch (userChoice) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        createOrder();
                        break;
                    case 3:
                        io.print("Editing an Order");
                        break;
                    case 4:
                        io.print("Removing an Order");
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
            }catch (Exception e){
                view.displayErrorMessage(e.getMessage());
                view.displayErrorMessage("An unseen error has occured!");
            }
        }
        exitMessage();
    }

    private void exitMessage(){
        view.displayExitBanner();
    }

    private void displayOrders(){
        view.displayDisplayAllBanner();
        String dateInput = view.getOrderDate();
        try {
            List<String> listOfOrders = dao.listOfOrders(dateInput);
            view.displayAllOrderList(listOfOrders);
        }catch (FlooringMasteryNoSuchFileException | FlooringMasteryFileException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void orderPlacedMessage(){
        view.displayOrderPlacedBanner();
    }

    private void orderNotPlacedBaner(){
        view.displayOrderNotPlacedBanner();
    }

    private int getUserSelection() {
        return view.printOptionsAndGetSelection();
    }

    private void createOrder(){
        view.displayCreateOrderBanner();
        io.print("Type 'q' if you ever want to leave!");
        try{
            // Read data from Products.txt and Taxes.txt and place them into a HashMap for easier access
            dao.loadDataIntoHashMaps();

            while (true){
                try{
                    // Get the customer name
                    String customerName = io.readString("Please enter a customer name");
                    if(customerName.equalsIgnoreCase("q")){ break; }
                    if(!dao.isValidCustomerName(customerName)) throw new FlooringMasteryBadDataException("Name may not be blank and is limited to characters [a-zA-Z][0-9], periods, and comma");


                    // Ask the customer for a valid state
                    Set<String> states = dao.getStateMapKeysAsSet();
                    io.printF("States we do business with: [%s]\n", io.printHashSet(states));
                    String pickedState = io.readString("Please pick a state");
                    if(pickedState.equalsIgnoreCase("q")){ break;}
                    if(!dao.isValidState(pickedState)) throw new FlooringMasteryBadDataException("We don't do business with " + pickedState);

                    // List products and get user input
                    Map<String, Product> listOfProducts = dao.getProductMap();
                    view.displayAllProducts(listOfProducts);
                    String userChoice = io.readString("Please type in the product type");
                    if(userChoice.equalsIgnoreCase("q")){break;}
                    if(!dao.isValidProductType(userChoice)) throw new FlooringMasteryBadDataException("No such product named " + userChoice.substring(0, 1).toUpperCase() + userChoice.substring(1));


                    // Ask the user for the area
                    String areaString = io.readString("Please enter a POSITIVE decimal for area you want work with. Minimum order size is 100 sq ft");
                    if(areaString.equalsIgnoreCase("q")){break;}
                    BigDecimal area = new BigDecimal(areaString);
                    if(area.compareTo(BigDecimal.ZERO) < 0 || area.compareTo(new BigDecimal("100")) < 0) throw new FlooringMasteryBadDataException("Please enter a positive value and minimum order is 100 sq ft.");

                    Product product = dao.getProduct(userChoice.substring(0, 1).toUpperCase() + userChoice.substring(1));
                    Tax tax = dao.getTax(pickedState.toUpperCase().replaceAll("\\s", ""));

                    Order order = computeCost(customerName, product, tax, area);

                    view.printOrderSummary(order);
                    String userConfirmation = io.readString("If you want to place the order, type 'y', else any other input will cancel the order");

                    if(userConfirmation.equalsIgnoreCase("y")){
                        placeOrder(order);
                        orderPlacedMessage();
                        break;
                    }
                    orderNotPlacedBaner();

                    break;
                }
//                catch (FlooringMasteryBadDataException e){
                catch (Exception e){
                    view.displayErrorMessage(e.getMessage());
                }
            }

        }
        catch (FlooringMasteryFileException | FlooringMasteryNoSuchFileException e){
            view.displayErrorMessage(e.getMessage());
        }
    }

    private Order computeCost(String customerName, Product product, Tax tax, BigDecimal area) throws FlooringMasteryFileException {
        // Get the OrderNumber
        String line;
        int orderNumber = 0;

        try{
            BufferedReader reader = new BufferedReader(new FileReader(FILENAME));
            String lineCount = reader.readLine();
            if(lineCount != null){
                orderNumber = Integer.parseInt(lineCount);
            }
        }
        catch (Exception e){
            throw new FlooringMasteryFileException("No such file exist");
        }
        return new Order(orderNumber, customerName, tax.getStateAbbreviation(), tax.getTaxRate(), product.getProductType(), area, product.getCostPerSquareFoot(), product.getLaborCostPerSquareFoot());
    }

    private void placeOrder(Order order){
        String order_dir = "Orders";
        String header = "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total";

        String fileName = String.format("%s/Orders_%s%s", order_dir, order.getOrderDate(), ".txt");
        File orderFile = new File(fileName);
        boolean fileExists = orderFile.exists();

        try (FileWriter fileWriter = new FileWriter(orderFile, true)) {
            if (!fileExists) {
                fileWriter.write(header);
                fileWriter.write(System.lineSeparator());
            }

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
            fileWriter.close();

            // Increment order number in order file
            int count = order.getOrderNumber();
            count++;

            try{
                BufferedWriter incrementWriter = new BufferedWriter(new FileWriter("Data/OrderNumber.txt"));
                incrementWriter.write(Integer.toString(count));
                incrementWriter.close();
            }
            catch (Exception e){

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
