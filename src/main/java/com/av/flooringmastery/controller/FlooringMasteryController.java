package com.av.flooringmastery.controller;

import com.av.flooringmastery.dao.FlooringMasteryDao;
import com.av.flooringmastery.dao.FlooringMasteryFileException;
import com.av.flooringmastery.dao.FlooringMasteryNoSuchFileException;
import com.av.flooringmastery.dto.Order;
import com.av.flooringmastery.service.FlooringMasteryServiceLayer;
import com.av.flooringmastery.ui.FlooringMasteryView;
import com.av.flooringmastery.ui.UserIO;
import com.av.flooringmastery.ui.UserIOConsoleImpl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FlooringMasteryController {

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

        try{
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
            }
            exitMessage();
        }
        catch (FlooringMasteryNoSuchFileException e){
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void exitMessage(){
        view.displayExitBanner();
    }

    private void displayOrders() throws FlooringMasteryNoSuchFileException {
        view.displayDisplayAllBanner();
        String dateInput = view.getOrderDate();
        try {
            List<String> listOfOrders = dao.listOfOrders(dateInput);
            view.displayAllOrderList(listOfOrders);
        }catch (FlooringMasteryNoSuchFileException | FlooringMasteryFileException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private int getUserSelection() {
        return view.printOptionsAndGetSelection();
    }

    // TODO: CREATE ORDER!!!!
    private void createOrder(){
        view.displayCreateOrderBanner();
        Order currentOrder = view.getNewOrderInfo();

        dao.addOrder(currentOrder);

    }
}
