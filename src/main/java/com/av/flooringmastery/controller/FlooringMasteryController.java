package com.av.flooringmastery.controller;

import com.av.flooringmastery.dao.FlooringMasteryDao;
import com.av.flooringmastery.dto.Order;
import com.av.flooringmastery.service.FlooringMasteryServiceLayer;
import com.av.flooringmastery.ui.FlooringMasteryView;
import com.av.flooringmastery.ui.UserIO;
import com.av.flooringmastery.ui.UserIOConsoleImpl;

import java.io.*;
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

        outer:
        while (true) {
            userChoice = getUserSelection();

            switch (userChoice) {
                case 1:
                    // TODO: REFACTOR THIS
                    final String ORDER_FOLDER = "Orders";
                    File folder = new File(ORDER_FOLDER);
                    File[] files = folder.listFiles();

                    int index = 0;

                    for(File file: files){
                        if(file.isFile()){
                            System.out.println(file.getName());
                        }
                    }

                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Enter a date (MM/DD/YYYY): ");
                    String dateInput = scanner.nextLine();

                    String targetFileName = "Orders_" + dateInput.replace("/","") + ".txt";
                    File targetFile = null;
                    for(File file: files){
                        if(file.isFile() && file.getName().equals(targetFileName)){
                            targetFile = file;
                            break;
                        }
                    }


                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(targetFile));
//                        Scanner scanner1 = new Scanner(targetFile);

//                        while(scanner1.hasNextLine()){
//                            String line = scanner1.nextLine();
//                            System.out.println(line);
//                        }
                        String line;
                        while((line = reader.readLine()) != null){
                            System.out.println(line);
                        }

                        reader.close();
                    }catch (FileNotFoundException e){
                        System.out.println("File not found: " + targetFile);
                    }
                    catch (IOException e){
                        System.out.println("Error reading file: " + targetFile);
                    }
                    catch (Exception e){
                        System.out.println("No such file?");
                    }


                    //displayOrder();
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
    }

    private int getUserSelection() {
        return view.printOptionsAndGetSelection();
    }
    // WILL ASK THE USER FOR THE DATE
    private void displayOrder(){
        view.displayDisplayAllBanner();
        String orderDateString = view.getOrderDate();
        System.out.println(orderDateString);


    }

    // TODO: CREATE ORDER!!!!
    private void createOrder(){
        view.displayCreateOrderBanner();
        Order currentOrder = view.getNewOrderInfo();

        dao.addOrder(currentOrder);

    }
}
