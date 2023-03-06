package com.av.flooringmastery.service;

import com.av.flooringmastery.dao.FlooringMasteryDao;
import com.av.flooringmastery.dao.FlooringMasteryFileException;
import com.av.flooringmastery.dao.FlooringMasteryNoSuchFileException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FlooringMasteryServiceLayer implements FlooringMasteryServiceLayerImpl{
    private String pattern = "MMddyyyy";

    FlooringMasteryDao dao;

    public FlooringMasteryServiceLayer(FlooringMasteryDao dao) {
        this.dao = dao;
    }

    @Override
    public List<String> displayOrders(String dateInput) throws FlooringMasteryException{
        // Validate the date first
        if(!isDateValid(dateInput)) throw new FlooringMasteryException("Date inputted doesn't follow " + pattern + " format");

        // Check to see if we even get anything
        if(dao.listOfOrders(dateInput) == null){
            throw new FlooringMasteryException(dateInput + " is invalid");
        }
        return dao.listOfOrders(dateInput);
    }

    private boolean isDateValid(String dateString) throws FlooringMasteryException{
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        // Set lenient to false, so it will ONLY accept dates in the correct format
        dateFormat.setLenient(false);

        try{
            Date date = dateFormat.parse(dateString);

            if(dateFormat.format(date).equals(dateString)){
                return true;
            }

        } catch (ParseException e) {
            return false;
        }
        return false;
    }


}
