package com.av.flooringmastery.dao;

import com.av.flooringmastery.dto.Order;
import com.av.flooringmastery.dto.Product;
import com.av.flooringmastery.dto.Tax;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FlooringMasteryDaoImpl implements FlooringMasteryDao{

    public static final String DELIMITER = ",";
    private final String ORDER_FOLDER = "Orders";
    private final String DATA_FOLDER = "Data";
    private final String TAX_FILE_NAME = "Taxes.txt";
    private final String PRODUCT_FILE_NAME = "Products.txt";

    private List<Product> products = new ArrayList<>();

    // Keep it simple and just put all the orders here
    private Map<Integer, Order> orderMap = new HashMap<>();

    // TODO: PLACE OUR TAX INFO HERE
    private HashMap<String, Tax> taxMap = new HashMap<>();
    private Map<String, Product> productMap = new HashMap<>();


    @Override
    public Order addOrder(Order order) {
        Order newOrder = orderMap.put(order.getOrderNumber(), order);
        return newOrder;
    }

    @Override
    public List<Order> getAllOrders() {
        return new ArrayList(orderMap.values());
    }

    @Override
    public Order getOrder(Product product) {
        return null;
    }

    @Override
    public Order removeOrder(Product product) {
        return null;
    }

    // TODO: Change exception to custom exception
    public void loadProductsFromFile() throws IOException {
        String filePath = Paths.get("Data", "Products").toString();

        List<Product> products = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(DELIMITER);
                String productType = values[0];
                BigDecimal costPerSquareFoot = new BigDecimal(values[1]);
                BigDecimal laborCostPerSquareFoot = new BigDecimal(values[2]);

                Product product = new Product(productType, costPerSquareFoot, laborCostPerSquareFoot);
                products.add(product);
            }
            // TODO: NEED TO MAKE A CUSTOM EXCEPTION
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getProductArray(){
        return new ArrayList(products);
    }

    @Override
    public List<String> listOfOrders(String dateInput) throws FlooringMasteryNoSuchFileException, FlooringMasteryFileException {
        File targetFile = locateAndGetOrderFile(ORDER_FOLDER, dateInput);

        if(targetFile == null) throw new FlooringMasteryNoSuchFileException("No such file exist");

        ArrayList<String> list = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(targetFile));
            String line;

            while((line = reader.readLine()) != null){
                list.add(line);
            }

            reader.close();
        } catch (Exception e){
            throw new FlooringMasteryFileException("Error with file handling");
        }

        return list;
    }

    @Override
    public void loadTaxDataIntoHashMap() throws FlooringMasteryFileException,  FlooringMasteryNoSuchFileException{
        File file = getFileFromFolder(DATA_FOLDER, TAX_FILE_NAME);

        if(!file.exists()) throw new FlooringMasteryNoSuchFileException("No such file called " + TAX_FILE_NAME);

        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            reader.readLine(); // Skip the first line that contains our headers
            while((line = reader.readLine()) != null){
                String[] fields = line.split(DELIMITER);

                String state = fields[0];
                String stateName = fields[1];
                BigDecimal taxRate = new BigDecimal(fields[2]);
                Tax tax = new Tax(state, stateName, taxRate);
                taxMap.put(tax.getStateAbbreviation(), tax);

            }
        }catch (Exception e){
            throw new FlooringMasteryFileException("File error handling with " + TAX_FILE_NAME);
        }
    }

    @Override
    public void loadProductDataIntoHashMap() throws FlooringMasteryFileException,  FlooringMasteryNoSuchFileException{
        File file = getFileFromFolder(DATA_FOLDER, PRODUCT_FILE_NAME);

        if(!file.exists()) throw new FlooringMasteryNoSuchFileException("No such fule called " + PRODUCT_FILE_NAME);

        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            reader.readLine();

            while((line = reader.readLine()) != null){
                String[] fields = line.split(DELIMITER);
                Product product = new Product(fields[0], new BigDecimal(fields[1]), new BigDecimal(fields[2]));
                productMap.put(product.getProductType(), product);
            }
        }catch (Exception e){
            throw new FlooringMasteryFileException("File error handling with " + PRODUCT_FILE_NAME);
        }
    }

    private File getFileFromFolder(String parentFolder, String fileName) {
        File folder = new File(parentFolder);
        File file = new File(folder, fileName);
        return file;
    }

    private File locateAndGetOrderFile(String folderName, String dateInput) {
        File folder = new File(folderName);
        File[] files = folder.listFiles();

        String targetFileName = "Orders_" + dateInput.replace("/","") + ".txt";
        File targetFile = null;

        for(File file: files){
            if(file.isFile() && file.getName().equals(targetFileName)){
                targetFile = file;
                break;
            }
        }
        return targetFile;
    }
}
