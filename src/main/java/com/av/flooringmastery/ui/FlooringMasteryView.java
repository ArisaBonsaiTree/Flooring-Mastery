package com.av.flooringmastery.ui;

import com.av.flooringmastery.dto.Order;
import com.av.flooringmastery.dto.Product;

import java.util.List;
import java.util.Map;

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


    public void displayDisplayAllBanner() {
        io.print("=== Display All Orders");
    }

    public String getOrderDate() {
        return io.readString("Enter a date MMDDYYYY]: ");
    }

    public String getOrderNumber(){
        return io.readString("Enter the order number");
    }




    public void displayAllProducts(Map<String, Product> listOfProducts){
        String[] header = {"ProductType", "CostPerSquareFoot" , "LaborCostPerSquareFoot"};
        for(String column: header){
            io.printF("%20s", centerText(column, 20));
        }

        io.printEmptyLine();


        for(String s: listOfProducts.keySet()){
            Product product = listOfProducts.get(s);

            io.printF("%20s", centerText(product.getProductType(), 20));
            io.printF("%20s", centerText(product.getCostPerSquareFoot().toString(), 20));
            io.printF("%20s", centerText(product.getLaborCostPerSquareFoot().toString(), 20));
            io.printEmptyLine();
        }
    }

    // TODO: Print order summary
    public void printOrderSummary(Order order){
        io.printF("%20s", centerText("Order Number", 20));
        io.printF("%20s", centerText("Customer Name", 20));
        io.printF("%20s", centerText("Product Type", 20));
        io.printF("%20s", centerText("Total ($/USD)", 20));
        io.printEmptyLine();
        io.printF("%20s", centerText(order.getOrderNumber().toString(), 20));
        io.printF("%20s", centerText(order.getCustomerName(), 20));
        io.printF("%20s", centerText(order.getProductType(), 20));
        io.printF("%20s", centerText(order.getTotal().toString(), 20));
        io.printEmptyLine();
    }



    public void displayAllOrderList(List<String> listOfOrders) {
        if (listOfOrders.size() <= 1) {
            io.print("No orders in this file");
            return;
        }

        String[] header = listOfOrders.get(0).split(",");
        for (String column : header) {
            io.printF("%20s", centerText(column, 20));
        }
        io.printEmptyLine();

        // Print the data rows
        for (int i = 1; i < listOfOrders.size(); i++) {
            String[] row = listOfOrders.get(i).split(",");
            for (String column : row) {
                io.printF("%20s", centerText(column, 20));
            }
            io.printEmptyLine();
        }
        io.readString("Press any key to continue");
    }


    private String centerText(String text, int width) {
        if (width <= text.length()) {
            return text;
        }
        int padding = width - text.length();
        int leftPadding = padding / 2;
        int rightPadding = padding - leftPadding;

        return String.format("%s%s%s", " ".repeat(leftPadding), text, " ".repeat(rightPadding));
    }

    public void displayErrorMessage(String errorMessage) {
        io.error("=== ERROR ===");
        io.error(errorMessage);
        io.error("=== +++++ ===");
    }

    public void displayExitBanner() {
        io.print("Good Bye!!!");
    }

    public void displayCreateSuccessBanner() {
        io.readString("Order successfully placed. Please hit enter to continue");
    }

    public void displayOrderNotPlacedBanner() {
        io.print("Order NOT Placed!");
    }

    public void displayOrderPlacedBanner() {
        io.print("Order Placed!");
    }

    public void displayOrderEditedBanner() {
        io.print("Order Edited!");
    }

    public void displayOrderNotEditedBanner() {
        io.print("Order NOT Edited!");
    }

    public void displayOrderDeletedBanner() {
        io.print("Order DELETED");
    }

    public void displayOrderNotDeletedBanner() {
        io.print("Order NOT deleted");
    }
}
