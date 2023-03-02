package com.av.flooringmastery.ui;

import com.av.flooringmastery.dto.Order;

import java.math.BigDecimal;
import java.util.List;

public class FlooringMasteryView {

    // We could just get the size of the Order Map!
    private static int orderId = 1;

    private UserIO io;
    private final int MIN_CHOICE = 1;
    private final int MAX_CHOICE = 6;

    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }

    public int printOptionsAndGetSelection() {
        io.print("<<Flooring Program>>");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("5. Export All Data");
        io.print("6. Quit");

        return io.readInt("Please select from the above choices.", MIN_CHOICE, MAX_CHOICE);
    }

    public void displayCreateOrderBanner() {
    }

    // TODO: IMPLEMENT LATER
    public Order getNewOrderInfo() {
        int orderNumber = orderId++; // Increment it after

        String customerName = io.readString("Your name");
        // TODO: Users will only input CustomerName(Must be valid), State, Product Type, and AREA

        // TODO: Create a STATE file to load the data!
        // For state we will need to verify there is a tax code!
        String state = io.readString("State? (Abbreviations/Full Name)");
        return null;

    }

    public void displayDisplayAllBanner() {
        io.print("=== Display All Orders");
    }

    public String getOrderDate() {
        return io.readDateString("Please enter the order date:");
    }


}
