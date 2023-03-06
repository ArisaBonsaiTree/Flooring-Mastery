package com.av.flooringmastery;

import com.av.flooringmastery.controller.FlooringMasteryController;
import com.av.flooringmastery.dao.FlooringMasteryDao;
import com.av.flooringmastery.dao.FlooringMasteryDaoImpl;
import com.av.flooringmastery.service.FlooringMasteryServiceLayerImpl;
import com.av.flooringmastery.ui.FlooringMasteryView;
import com.av.flooringmastery.ui.UserIO;
import com.av.flooringmastery.ui.UserIOConsoleImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class App {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        FlooringMasteryController controller = ctx.getBean("controller", FlooringMasteryController.class);
        controller.run();
    }
}
