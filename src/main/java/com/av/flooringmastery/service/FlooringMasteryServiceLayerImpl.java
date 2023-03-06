package com.av.flooringmastery.service;

import com.av.flooringmastery.dao.FlooringMasteryFileException;
import com.av.flooringmastery.dao.FlooringMasteryNoSuchFileException;

import java.util.List;

public interface FlooringMasteryServiceLayerImpl {

    List<String> displayOrders(String s) throws FlooringMasteryException, FlooringMasteryNoSuchFileException, FlooringMasteryFileException;

}
