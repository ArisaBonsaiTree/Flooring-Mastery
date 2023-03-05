package com.av.flooringmastery.dto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private final String FILE_LOCATION = "Data/Products.txt";
    private final String DELIMITER = ",";

    private Integer orderNumber;
    private String orderDate;
    private String customerName;
    private String state;

    private BigDecimal taxRate;

    private String productType;

    private BigDecimal area;
    private BigDecimal costPerSquareFoot;
    private BigDecimal laborCostPerSquareFoot;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal tax;
    private BigDecimal total;

    public Order() {
        // Default gang
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    // Create an order object for flooring mastery
    public Order(Integer orderNumber, String customerName, String state, BigDecimal taxRate, String productType, BigDecimal area,
                 BigDecimal costPerSquareFoot, BigDecimal laborCostPerSquareFoot){
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.state = state;
        this.taxRate = taxRate;
        this.productType = productType;
        this.area = area;
        this.costPerSquareFoot = costPerSquareFoot;
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;


        // Compute MaterialCost
        BigDecimal materialCost = area.multiply(costPerSquareFoot);

        // Compute LaborCost
        BigDecimal laborCost = area.multiply(laborCostPerSquareFoot);

        // Compute Tax
        BigDecimal tax = materialCost.add(laborCost).multiply(taxRate.divide(BigDecimal.valueOf(100)));

        // Compute Total
        BigDecimal total = materialCost.add(laborCost).add(tax);

        // Set the values to the corresponding instance variables
        this.materialCost = materialCost.setScale(2, RoundingMode.HALF_UP);
        this.laborCost = laborCost.setScale(2, RoundingMode.HALF_UP);
        this.tax = tax.setScale(2, RoundingMode.HALF_UP);
        this.total = total.setScale(2, RoundingMode.HALF_UP);

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");

        this.orderDate = tomorrow.format(formatter);
    }

    public Order(Integer orderNumber, String customerName, String state, BigDecimal taxRate, String productType, BigDecimal area, BigDecimal costPerSquareFoot, BigDecimal laborCostPerSquareFoot, BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax, BigDecimal total) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.state = state;
        this.taxRate = taxRate;
        this.productType = productType;
        this.area = area;
        this.costPerSquareFoot = costPerSquareFoot;
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
        this.materialCost = materialCost;
        this.laborCost = laborCost;
        this.tax = tax;
        this.total = total;
    }

    public void setMaterialCost(BigDecimal area, BigDecimal costPerSquareFoot) {

        this.materialCost = area.multiply(costPerSquareFoot);
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot;
    }

    public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
        this.costPerSquareFoot = costPerSquareFoot;
    }

    public BigDecimal getLaborCostPerSquareFoot() {
        return laborCostPerSquareFoot;
    }

    public void setLaborCostPerSquareFoot(BigDecimal laborCostPerSquareFoot) {
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getOrderDate() {
        return orderDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "FILE_LOCATION='" + FILE_LOCATION + '\'' +
                ", DELIMITER='" + DELIMITER + '\'' +
                ", orderNumber=" + orderNumber +
                ", customerName='" + customerName + '\'' +
                ", state='" + state + '\'' +
                ", taxRate=" + taxRate +
                ", productType='" + productType + '\'' +
                ", area=" + area +
                ", costPerSquareFoot=" + costPerSquareFoot +
                ", laborCostPerSquareFoot=" + laborCostPerSquareFoot +
                ", materialCost=" + materialCost +
                ", laborCost=" + laborCost +
                ", tax=" + tax +
                ", total=" + total +
                '}';
    }
}
