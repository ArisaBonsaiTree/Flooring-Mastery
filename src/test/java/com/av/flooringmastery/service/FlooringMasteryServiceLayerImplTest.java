package com.av.flooringmastery.service;

import com.av.flooringmastery.dto.Order;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    void editOrder(){
        try{
            serviceLayer.editOrder("06022013", "2");
        }
        catch (FlooringMasteryException e){
            fail("Valid edit input");
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

    @Test
    void testDeleteOrder() throws FlooringMasteryException{

        Order order = serviceLayer.deleteOrder("01012023", "20");

        assertNotNull(order, "Removing 20 should not be null");

        Order orderTwo = null;
        try {
            orderTwo = serviceLayer.deleteOrder("01012023", "24432432423324234243");
        }catch (FlooringMasteryException e){
            assertNull(orderTwo, "Removing 24432432423324234243 should be null");
        }
    }

}
