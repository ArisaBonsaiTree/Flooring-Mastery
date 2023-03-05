import java.io.*;

public class CounterProgram {
    private static final String FILENAME = "Data/OrderNumber.txt";
    private static int count = 0;

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        boolean quit = false;

        // read current count from file
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILENAME));
            String lineCount = reader.readLine();
            if (lineCount != null) {
                count = Integer.parseInt(lineCount);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        while (!quit) {
            System.out.println("Current count: " + count);
            System.out.println("1. View number");
            System.out.println("2. Increment number");
            System.out.println("3. Leave program");

            try {
                line = br.readLine();
            } catch (IOException e) {
                System.out.println("Error reading input: " + e.getMessage());
                continue;
            }

            switch (line) {
                case "1":
                    System.out.println("Current count: " + count);
                    break;
                case "2":
                    count++;
                    System.out.println("Count incremented. Current count: " + count);
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME));
                        writer.write(Integer.toString(count));
                        writer.close();
                    } catch (Exception e) {
                        System.out.println("Error writing file: " + e.getMessage());
                    }
                    break;
                case "3":
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid input.");
                    break;
            }
        }

        System.out.println("Exiting program.");
    }
}
