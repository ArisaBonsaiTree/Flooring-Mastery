package com.av.flooringmastery.dao;

import com.av.flooringmastery.dto.Order;
import com.av.flooringmastery.dto.Product;
import com.av.flooringmastery.dto.Tax;
import com.av.flooringmastery.service.FlooringMasteryException;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

public class FlooringMasteryDaoImpl implements FlooringMasteryDao {

    public static final String DELIMITER = ",";
    private final String ORDER_FOLDER = "Orders";
    private final String DATA_FOLDER = "Data";
    private final String TAX_FILE_NAME = "Taxes.txt";
    private final String PRODUCT_FILE_NAME = "Products.txt";
    private final String DATE_FORMAT = "MMddyy";

    private List<Product> products = new ArrayList<>();

    // Keep it simple and just put all the orders here
    private Map<Integer, Order> orderMap = new HashMap<>();

    // TODO: PLACE OUR TAX INFO HERE
    private HashMap<String, Tax> taxMap = new HashMap<>();
    private Map<String, Product> productMap = new HashMap<>();

    private LinkedHashMap<String, Order> ordersByDate = new LinkedHashMap<>();


    public LinkedHashMap<String, Order> getOrdersByDate() {
        return ordersByDate;
    }


    @Override
    public void setOrdersByDate(LinkedHashMap<String, Order> ordersByDate) {
        this.ordersByDate = ordersByDate;
    }



    @Override
    public Order addOrder(Order order) {
        Order newOrder = orderMap.put(order.getOrderNumber(), order);
        return newOrder;
    }

    @Override
    public Map<String, Product> getProductMap(){
        return productMap;
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

    public List<Product> getProductArray() {
        return new ArrayList(products);
    }

    @Override
    public List<String> listOfOrders(String dateInput) throws FlooringMasteryException {
        File targetFile = locateAndGetOrderFile(ORDER_FOLDER, dateInput);

        if (targetFile == null) throw new FlooringMasteryException("No such file exist");

        ArrayList<String> list = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(targetFile));
            String line;

            while ((line = reader.readLine()) != null) {
                if(!line.contains("-")){
                    list.add(line);
                }

            }

            reader.close();
        } catch (Exception e) {
            throw new FlooringMasteryException("Error with file handling");
        }

        return list;
    }



    @Override
    public void loadDataIntoHashMaps() throws FlooringMasteryException {
        loadProductDataIntoHashMap();
        loadTaxDataIntoHashMap();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Map<Integer, Order> getOrderMap() {
        return orderMap;
    }

    public void setOrderMap(Map<Integer, Order> orderMap) {
        this.orderMap = orderMap;
    }

    public HashMap<String, Tax> getTaxMap() {
        return taxMap;
    }

    public void setTaxMap(HashMap<String, Tax> taxMap) {
        this.taxMap = taxMap;
    }

    public void setProductMap(Map<String, Product> productMap) {
        this.productMap = productMap;
    }


    private void loadTaxDataIntoHashMap() throws FlooringMasteryException {
        File file = getFileFromFolder(DATA_FOLDER, TAX_FILE_NAME);

        if (!file.exists()) throw new FlooringMasteryException("No such file called " + TAX_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine(); // Skip the first line that contains our headers
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(DELIMITER);

                String state = fields[0];
                String stateName = fields[1];
                BigDecimal taxRate = new BigDecimal(fields[2]);
                Tax tax = new Tax(state, stateName, taxRate);
                taxMap.put(tax.getStateAbbreviation(), tax);
            }
        } catch (Exception e) {
            throw new FlooringMasteryException("File error handling with " + TAX_FILE_NAME);
        }
    }

    private void loadProductDataIntoHashMap() throws FlooringMasteryException{
        File file = getFileFromFolder(DATA_FOLDER, PRODUCT_FILE_NAME);

        if (!file.exists()) throw new FlooringMasteryException("No such file called " + PRODUCT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(DELIMITER);
                Product product = new Product(fields[0], new BigDecimal(fields[1]), new BigDecimal(fields[2]));
                productMap.put(product.getProductType(), product);
            }
        } catch (Exception e) {
            throw new FlooringMasteryException("File error handling with " + PRODUCT_FILE_NAME);
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

        String targetFileName = "Orders_" + dateInput.replace("/", "") + ".txt";
        File targetFile = null;

        for (File file : files) {
            if (file.isFile() && file.getName().equals(targetFileName)) {
                targetFile = file;
                break;
            }
        }
        return targetFile;
    }


    public String getDate(boolean isTomorrow) {
        LocalDate date = LocalDate.now();
        if (isTomorrow) {
            date = date.plusDays(1);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return date.format(formatter);
    }

    @Override
    public boolean isValidState(String stateName) {
        return taxMap.containsKey(stateName.toUpperCase());
    }

    @Override
    public boolean isValidProductType(String productName){
        return productMap.containsKey(productName.substring(0, 1).toUpperCase() + productName.substring(1));
    }

    @Override
    public boolean isValidCustomerName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }

        String regex = "^[a-zA-Z0-9.,\\s]+$";
        return name.matches(regex);
    }

    @Override
    public Product getProduct(String productKey) {
        return productMap.get(productKey);
    }

    @Override
    public Tax getTax(String stateKey) {
        return taxMap.get(stateKey);
    }

    @Override
    public Set<String> getHashMapKeysAsSet(HashMap<String, ?> map) {
        Set<String> keySet = map.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        return new HashSet<>(Arrays.asList(keyArray));
    }

    @Override
    public Set<String> getStateMapKeysAsSet() {
        Set<String> keySet = taxMap.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        return new HashSet<>(Arrays.asList(keyArray));
    }
}
