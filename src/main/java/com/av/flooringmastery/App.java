package com.av.flooringmastery;

import com.av.flooringmastery.controller.FlooringMasteryController;
import com.av.flooringmastery.dao.FlooringMasteryDao;
import com.av.flooringmastery.dao.FlooringMasteryDaoImpl;
import com.av.flooringmastery.ui.FlooringMasteryView;
import com.av.flooringmastery.ui.UserIO;
import com.av.flooringmastery.ui.UserIOConsoleImpl;

public class App {
    public static void main(String[] args) {

        UserIO io = new UserIOConsoleImpl();
        FlooringMasteryView view = new FlooringMasteryView(io);

        FlooringMasteryDao dao = new FlooringMasteryDaoImpl();

        FlooringMasteryController controller = new FlooringMasteryController(view, dao);

        controller.run();
    }
}
