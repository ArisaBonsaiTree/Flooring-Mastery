package com.av.flooringmastery.controller;

import com.av.flooringmastery.dto.Order;
import com.av.flooringmastery.dto.Product;
import com.av.flooringmastery.service.FlooringMasteryException;
import com.av.flooringmastery.service.FlooringMasteryServiceLayerImpl;
import com.av.flooringmastery.ui.FlooringMasteryView;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FlooringMasteryController {
    private FlooringMasteryView view;
    private FlooringMasteryServiceLayerImpl service;

    public FlooringMasteryController(FlooringMasteryView view, FlooringMasteryServiceLayerImpl service) {
        this.view = view;
        this.service = service;
    }

    public void run() {
        int userChoice;

        outer:
        while (true) {
            userChoice = getUserSelection();

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
                    exportOrders();
                    break;
                case 6:
                    break outer;
                default:
                    unknownCommand();
            }
        }
        exitMessage();
    }

    private void exportOrders(){
        view.displayBackingUpDateBanner();
        try {
            service.backupData();
            view.displayBackupSuccessBanner();
        }
        catch (FlooringMasteryException e){
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void unknownCommand(){
        view.displayUnknownComnmmandBanner();
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
                    view.displayOrderPlacedBanner();
                }

                else{
                    view.displayOrderNotPlacedBanner();
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
                String userConfirmation = view.deleteOrderUserConfirmation();

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

    private void editOrder(){
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
                String userConfirmation = view.editOrderUserConfirmation();

                if (userConfirmation.equalsIgnoreCase("y")) {
                    service.editMapAndOverride(orderToBeEdited, dateInput);
                    view.displayOrderEditedBanner();
                    return;
                }
                view.displayOrderNotEditedBanner();

                hasErrors = false;
            } catch (FlooringMasteryException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
                if (view.userWantsToLeave()) break;
            }
        } while (hasErrors);
    }

    private int getUserSelection() {
        return view.printOptionsAndGetSelection();
    }

}
