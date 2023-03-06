package com.av.flooringmastery.service;

import com.av.flooringmastery.dto.Order;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FlooringMasteryServiceLayerImplTest {

    private FlooringMasteryServiceLayer serviceLayer;

    public FlooringMasteryServiceLayerImplTest(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        serviceLayer = ctx.getBean("serviceLayer", FlooringMasteryServiceLayer.class);
    }

    @Test
    void createOrder(){
        Order order = new Order();
        order.setCustomerName("Lui");
        order.setState("CA");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("150"));

        try{
            serviceLayer.loadDataIntoHashMaps();
            serviceLayer.createOrderObject(order);
        } catch (FlooringMasteryException e) {
            fail("Student was valid. No exception should have bee thrown");
        }
    }

    @Test
    void testCreateInvalidData(){
        Order order = new Order();
        order.setCustomerName("Lui");
        order.setState("CA");
        order.setProductType("Wod");
        order.setArea(new BigDecimal("150"));

        try{
            serviceLayer.loadDataIntoHashMaps();
            serviceLayer.createOrderObject(order);
        }
        catch (FlooringMasteryException e){

        }
        catch (Exception e){
            fail("Incorrect Exception");
        }
    }

    @Test
    void testValidGetAllOrders(){
        try{
            List<String> listOfOrders = serviceLayer.displayOrders("01012020");
            assertEquals(1, listOfOrders.size());
        }
        catch (FlooringMasteryException e){
            fail("Valid command. No exception should have been thrown");
        }
    }

}
