/*
 * ------------------------------------------------------------
 * Names: Jana Abdulrahman   | 445810234
 *        Hanenn Saeed Ahmad (447802061)
 *        Fai Ali Alqahtani (445810696)
 * Group: 2
 * Project 1: Food Ordering Management System
 * Issues: None
 * AI Usage: AI was used only as a tutor to explain some ideas
 *           (title case, ID validation, try-catch). We wrote
 *           the code ourselves and we can explain every part.
 * ------------------------------------------------------------
 */

import java.util.Scanner;

public class FoodOrderingSystem {

    // constants used in the whole program
    static final int MAX_ITEMS = 5;                  // maximum items in one order
    static final double DELIVERY_FEE = 15.0;         // delivery fee in SAR
    static final double FREE_DELIVERY_LIMIT = 150.0; // orders >= 150 get free delivery (bonus)
    static final double STUDENT_DISCOUNT = 0.10;     // 10% deduction for students
    static final double VAT_RATE = 0.15;             // 15% VAT

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // ---- variables for the customer ----
        String customerName = "";
        String customerId = "";
        int customerType = 0; // 1 = Student, 2 = Regular
        int numberOfItems = 0;

        // ---- parallel arrays to store the order items ----
        String[] itemNames = new String[MAX_ITEMS];
        int[] quantities = new int[MAX_ITEMS];
        double[] unitPrices = new double[MAX_ITEMS];

        boolean detailsEntered = false;
        boolean itemsEntered = false;
        int choice = 0;

        while (choice != 5) {
            printMenu();
            choice = readInt(input, "Enter your choice: ");

            switch (choice) {
                case 1:
                    // ----- enter customer details -----
                    System.out.print("Enter Customer Name: ");
                    customerName = toTitleCase(input.nextLine());

                    // KKU ID must start with 4 and be exactly 9 digits
                    while (true) {
                        System.out.print("Enter ID Number: ");
                        customerId = input.nextLine().trim();
                        if (isValidId(customerId)) {
                            break;
                        }
                        System.out.println("Invalid ID! ID must start with 4 and contain exactly 9 digits.");
                    }

                    // number of items must be between 1 and MAX_ITEMS
                    while (true) {
                        numberOfItems = readInt(input, "How many items do you want to order? ");
                        if (numberOfItems >= 1 && numberOfItems <= MAX_ITEMS) {
                            break;
                        }
                        System.out.println("Invalid number! You can order between 1 and " + MAX_ITEMS + " items.");
                    }

                    // customer type (student gets a discount deduction)
                    while (true) {
                        customerType = readInt(input, "Customer type (1 = Student, 2 = Regular): ");
                        if (customerType == 1 || customerType == 2) {
                            break;
                        }
                        System.out.println("Please enter 1 for Student or 2 for Regular.");
                    }

                    detailsEntered = true;
                    itemsEntered = false; // new customer means a new order
                    System.out.println("Customer details saved successfully!");
                    break;

                case 2:
                    // ----- enter order items -----
                    if (!detailsEntered) {
                        System.out.println("Please enter customer details first (option 1).");
                        break;
                    }
                    System.out.println("Enter details for " + numberOfItems + " items:");
                    for (int i = 0; i < numberOfItems; i++) {
                        System.out.print("Item " + (i + 1) + " name: ");
                        itemNames[i] = input.nextLine().trim();

                        while (true) {
                            quantities[i] = readInt(input, "Quantity: ");
                            if (quantities[i] >= 1 && quantities[i] <= 20) {
                                break;
                            }
                            System.out.println("Invalid quantity! It must be between 1 and 20.");
                        }

                        while (true) {
                            unitPrices[i] = readDouble(input, "Unit price (SAR): ");
                            if (unitPrices[i] >= 1 && unitPrices[i] <= 500) {
                                break;
                            }
                            System.out.println("Invalid price! It must be between 1 and 500.");
                        }
                    }
                    itemsEntered = true;
                    // show the bill immediately after the last item, same as the marks program
                    displayOrderInfo(customerName, customerId, customerType,
                            itemNames, quantities, unitPrices, numberOfItems);
                    break;

                case 3:
                    // ----- display order info -----
                    if (!itemsEntered) {
                        System.out.println("No items entered yet. Use option 2 first.");
                    } else {
                        displayOrderInfo(customerName, customerId, customerType,
                                itemNames, quantities, unitPrices, numberOfItems);
                    }
                    break;

                case 4:
                    // ----- display customer details -----
                    if (!detailsEntered) {
                        System.out.println("No customer details entered yet. Use option 1 first.");
                    } else {
                        System.out.println("===== Customer Details =====");
                        System.out.println("Name: " + customerName);
                        System.out.println("KKU ID: " + customerId);
                        System.out.println("Type: " + (customerType == 1 ? "Student (10% discount)" : "Regular"));
                        if (itemsEntered) {
                            double subtotal = 0;
                            for (int i = 0; i < numberOfItems; i++) {
                                subtotal += quantities[i] * unitPrices[i];
                            }
                            System.out.printf("Current order subtotal: %.2f SAR (%d items)%n", subtotal, numberOfItems);
                        } else {
                            System.out.println("Order: no items entered yet.");
                        }
                    }
                    break;

                case 5:
                    System.out.println("Thank you for using the Food Ordering System. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice! Please enter a number between 1 and 5.");
            }
            System.out.println();
        }
        input.close();
    }

    // prints the main menu
    public static void printMenu() {
        System.out.println("Menu:");
        System.out.println("1. Enter Customer Details");
        System.out.println("2. Enter Order Items");
        System.out.println("3. Display Order Info.");
        System.out.println("4. Display Customer Details");
        System.out.println("5. Exit");
    }

    // reads an integer safely (no crash if the user types letters)
    public static int readInt(Scanner input, String message) {
        while (true) {
            System.out.print(message);
            String line = input.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a whole number.");
            }
        }
    }

    // reads a double safely
    public static double readDouble(Scanner input, String message) {
        while (true) {
            System.out.print(message);
            String line = input.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    // converts any name to Title Case and removes extra spaces
    public static String toTitleCase(String name) {
        name = name.trim().replaceAll("\\s+", " ");
        String[] words = name.split(" ");
        String result = "";
        for (String w : words) {
            if (w.length() > 0) {
                result += w.substring(0, 1).toUpperCase() + w.substring(1).toLowerCase() + " ";
            }
        }
        return result.trim();
    }

    // checks that the ID starts with 4 and has exactly 9 digits
    public static boolean isValidId(String id) {
        if (id.length() != 9) {
            return false;
        }
        if (id.charAt(0) != '4') {
            return false;
        }
        for (int i = 0; i < id.length(); i++) {
            if (!Character.isDigit(id.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // prints the full bill: items, bonus/deduction and totals
    public static void displayOrderInfo(String customerName, String customerId, int customerType,
                                        String[] itemNames, int[] quantities,
                                        double[] unitPrices, int numberOfItems) {
        double subtotal = 0;
        double[] lineTotals = new double[numberOfItems];

        System.out.println("===== Order Info =====");
        System.out.println("Name: " + customerName);
        System.out.println("KKU ID: " + customerId);
        System.out.println("----------------------------------------------------------");
        System.out.printf("%-15s %-8s %-10s %-10s %s%n", "Item", "Qty", "Price", "Total", "Note");
        for (int i = 0; i < numberOfItems; i++) {
            lineTotals[i] = quantities[i] * unitPrices[i];
            subtotal += lineTotals[i];
            String note = (lineTotals[i] >= 100) ? "Large" : "Normal";
            System.out.printf("%-15s %-8d %-10.2f %-10.2f %s%n",
                    itemNames[i], quantities[i], unitPrices[i], lineTotals[i], note);
        }
        System.out.println("----------------------------------------------------------");

        // bonus: free delivery for big orders
        double delivery = (subtotal >= FREE_DELIVERY_LIMIT) ? 0 : DELIVERY_FEE;

        // deduction: 10% discount for students
        double discount = (customerType == 1) ? subtotal * STUDENT_DISCOUNT : 0;

        double vat = (subtotal - discount) * VAT_RATE;
        double total = subtotal - discount + delivery + vat;

        // average, maximum and minimum of the item totals
        double max = lineTotals[0], min = lineTotals[0], sum = 0;
        int maxIndex = 0, minIndex = 0;
        for (int i = 0; i < numberOfItems; i++) {
            sum += lineTotals[i];
            if (lineTotals[i] > max) {
                max = lineTotals[i];
                maxIndex = i;
            }
            if (lineTotals[i] < min) {
                min = lineTotals[i];
                minIndex = i;
            }
        }
        double average = sum / numberOfItems;

        System.out.printf("Subtotal: %.2f SAR%n", subtotal);
        if (delivery == 0) {
            System.out.println("Delivery: FREE (bonus: order is " + (int) FREE_DELIVERY_LIMIT + " SAR or more)");
        } else {
            System.out.printf("Delivery fee: %.2f SAR%n", delivery);
        }
        if (discount > 0) {
            System.out.printf("Student discount (10%%): -%.2f SAR%n", discount);
        }
        System.out.printf("VAT (15%%): %.2f SAR%n", vat);
        System.out.printf("TOTAL: %.2f SAR%n", total);
        System.out.println("============================");
        System.out.printf("Average item total: %.2f SAR%n", average);
        System.out.printf("Max item: %s (%.2f SAR)%n", itemNames[maxIndex], max);
        System.out.printf("Min item: %s (%.2f SAR)%n", itemNames[minIndex], min);
    }
}