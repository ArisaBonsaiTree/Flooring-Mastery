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


    private void deleteAnOrder() {
        view.displayDeleteOrderBanner();
        boolean hasErrors;

        do{
            try{
                String dateInput = view.getOrderDate();
                String orderNumber = view.getOrderNumber();

                Order deleteOrder = service.deleteOrder(dateInput, orderNumber);
                view.printOrderSummary(deleteOrder);

                String userConfirmation = io.readString("If you want to place DELETE the order, type 'y', else any other input will NOT delete the order");

                if (userConfirmation.equalsIgnoreCase("y")) {
                    service.deleteOrder(deleteOrder, dateInput);
                    view.displayOrderDeletedBanner();
                    return;
                }

                view.displayOrderNotDeletedBanner();
                hasErrors = false;
            }
            catch (FlooringMasteryException e){
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
                if(view.userWantsToLeave()) break;
            }
        }while (hasErrors);
    }


    // TODO: NEXT
    private void editOrder() throws FlooringMasteryFileException {
        view.displayEditOrderBanner();
        boolean hasErrors;

        do {
            try {
                service.loadDataIntoHashMaps();

                String dateInput = view.getOrderDate();
                String orderNumber = view.getOrderNumber();

                Order editOrder = service.editOrder(dateInput, orderNumber);
                Set<String> statesWeDoService = service.getStates();
                Map<String, Product> productsWeOffer = service.getProducts();

                Order newOrderData = view.getEditInfo(editOrder, statesWeDoService, productsWeOffer);

                Order orderToBeEdited = service.compareAndEdit(editOrder, newOrderData);
                orderToBeEdited = service.computeNewCost(orderToBeEdited);

                view.printOrderSummary(orderToBeEdited);

                String userConfirmation = io.readString("If you want to edit the order, type 'y', else any other input will cancel the order");

                if (userConfirmation.equalsIgnoreCase("y")) {
                    service.editMapAndOverride(orderToBeEdited, dateInput);
                    orderEditedMessage();
                    return;
                }
                orderNotEditedMessage();

                hasErrors = false;
            } catch (FlooringMasteryException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
                if (view.userWantsToLeave()) break;
            }
        } while (hasErrors);
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

    private int getUserSelection() {
        return view.printOptionsAndGetSelection();
    }

    private void orderNotPlacedMessage() {
        view.displayOrderNotPlacedBanner();
    }

    private Order editNewCost(String customerName, Product product, Tax tax, BigDecimal area, Integer orderNumber) throws FlooringMasteryFileException {
        return new Order(orderNumber, customerName, tax.getStateAbbreviation(), tax.getTaxRate(), product.getProductType(), area, product.getCostPerSquareFoot(), product.getLaborCostPerSquareFoot());
    }
}
