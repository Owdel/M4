import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
/**
 *
 * @author Owdel
 */
public class Milestone {
  private static final Scanner scanner       = new Scanner(System.in);
    private static final String  PESO          = "PHP ";
    private static final double  RATE_PER_HOUR = 100.0;
    private static final int     MAX_HOURS     = 8;
    private static String        loggedInUsername = "";
 
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=============================");
            System.out.println("         MAIN MENU           ");
            System.out.println("=============================");
            System.out.println("1. Login");
            System.out.println("2. Create Account");
            System.out.println("3. Exit");
            System.out.println("=============================");
            System.out.print("Choose an option: ");
 
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": login();         break;
                case "2": createAccount(); break;
                case "3":
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1, 2, or 3.");
            }
        }
    }
 
    // ---------------------------------------------------------------
    // LOGIN
    // ---------------------------------------------------------------
    private static void login() {
        int triesLeft = 5;
        System.out.println("\n--- Login ---");
        while (triesLeft > 0) {
            try {
                System.out.print("Username: ");
                String username = scanner.nextLine().trim();
                System.out.print("Password: ");
                String password = scanner.nextLine().trim();
 
                User user = new User.Builder().username(username).password(password).build();
                String role = DatabaseHelper.login(user);
 
                if (role != null) {
                    loggedInUsername = user.getUsername();
                    System.out.println("\nLogin successful! Welcome, " + loggedInUsername + "!");
                    if (role.equals("admin")) showAdminMenu();
                    else showStudentMenu();
                    return;
                } else {
                    triesLeft--;
                    if (triesLeft > 0) {
                        System.out.println("Wrong username or password. (" + triesLeft + " remaining tries)");
                        System.out.println("1. Try Again");
                        System.out.println("2. Exit to Main Menu");
                        System.out.print("Choose an option: ");
                        String option = scanner.nextLine().trim();
                        if (option.equals("2")) {
                            System.out.println("Returning to main menu.");
                            return;
                        }
                    } else {
                        System.out.println("Too many failed attempts. Returning to main menu.");
                    }
                }
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        }
    }
 
    // ---------------------------------------------------------------
    // CREATE ACCOUNT
    // ---------------------------------------------------------------
    private static void createAccount() {
        System.out.println("\n--- Create Account ---");
        try {
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();
            if (DatabaseHelper.usernameExists(username)) {
                System.out.println("That username is already taken.");
                return;
            }
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();
            System.out.print("First Name: ");
            String firstName = scanner.nextLine().trim();
            System.out.print("Last Name: ");
            String lastName = scanner.nextLine().trim();
            System.out.print("Number: ");
            String number = scanner.nextLine().trim();
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            System.out.print("Section (1 to 8): ");
            String className = scanner.nextLine().trim();
            System.out.print("Grade Level: ");
            String gradeLvl = scanner.nextLine().trim();
 
            User user = new User.Builder().username(username).password(password).build();
            Student student = new Student.Builder()
                    .username(username).firstName(firstName).lastName(lastName)
                    .number(number).email(email).className(className).gradeLvl(gradeLvl)
                    .build();
 
            if (DatabaseHelper.insertUser(user, student))
                System.out.println("Account created successfully! You can now log in.");
            else
                System.out.println("Something went wrong. Account was not created.");
 
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
 
    // ---------------------------------------------------------------
    // STUDENT MENU
    // ---------------------------------------------------------------
    private static void showStudentMenu() {
        while (true) {
            System.out.println("\n=============================");
            System.out.println("       STUDENT MENU          ");
            System.out.println("=============================");
            System.out.println("1. View Kitchen Schedule");
            System.out.println("2. Add Kitchen Booking");
            System.out.println("3. Pay for Booking");
            System.out.println("4. Logout");
            System.out.println("=============================");
            System.out.print("Choose an option: ");
 
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": viewKitchenSchedule(); break;
                case "2": addKitchenEntry();     break;
                case "3": payForBooking();        break;
                case "4":
                    System.out.println("Logged out.");
                    loggedInUsername = "";
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
 
    // ---------------------------------------------------------------
    // ADMIN MENU
    // ---------------------------------------------------------------
    private static void showAdminMenu() {
        while (true) {
            System.out.println("\n=============================");
            System.out.println("        ADMIN MENU           ");
            System.out.println("=============================");
            System.out.println("1.  View Kitchen Schedule");
            System.out.println("2.  Edit Kitchen Booking");
            System.out.println("3.  Delete Kitchen Booking");
            System.out.println("4.  Approve Kitchen Booking");
            System.out.println("5.  Reject Kitchen Booking");
            System.out.println("6.  Generate Student Report");
            System.out.println("7.  Update Student Profile");
            System.out.println("8.  View Income Statement");
            System.out.println("9.  Logout");
            System.out.println("=============================");
            System.out.print("Choose an option: ");
 
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": viewKitchenSchedule();   break;
                case "2": editKitchenEntry();      break;
                case "3": deleteKitchenEntry();    break;
                case "4": approveKitchenBooking(); break;
                case "5": rejectKitchenBooking();  break;
                case "6": generateStudentReport(); break;
                case "7": updateStudentProfile();  break;
                case "8": FinanceManager.printIncomeStatement(); break;
                case "9":
                    System.out.println("Logged out.");
                    loggedInUsername = "";
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
 
    // ---------------------------------------------------------------
    // KITCHEN — SHARED
    // ---------------------------------------------------------------
    private static void viewKitchenSchedule() {
        System.out.println("\n--- Kitchen Schedule ---");
        List<String> entries = DatabaseHelper.getAllKitchenEntries();
        if (entries.isEmpty())
            System.out.println("No entries found.");
        else
            for (String entry : entries) System.out.println(entry);
    }
 
    // ---------------------------------------------------------------
    // KITCHEN — STUDENT: Add Booking
    // ---------------------------------------------------------------
    private static void addKitchenEntry() {
        System.out.println("\n--- Add Kitchen Booking ---");
        System.out.println("Rate: " + PESO + String.format("%,.2f", RATE_PER_HOUR) + "/hour | Maximum: " + MAX_HOURS + " hours");
        System.out.println("Schedule format: M/D/YYYY H:MMAM or H:MMPM  (e.g. 5/8/2026 1:00PM)");
        try {
            System.out.print("Kitchen ID (1-4): ");
            int kitchenId = Integer.parseInt(scanner.nextLine().trim());
            if (kitchenId < 1 || kitchenId > 4) { System.out.println("Invalid Kitchen ID. Must be between 1 and 4."); return; }
 
            System.out.print("Section (1-8): ");
            int section = Integer.parseInt(scanner.nextLine().trim());
            if (section < 1 || section > 8) { System.out.println("Invalid Section. Must be between 1 and 8."); return; }
 
            System.out.print("Schedule Start: ");
            String start = DatabaseHelper.parseInputDateTime(scanner.nextLine().trim());
 
            System.out.print("Schedule End:   ");
            String end = DatabaseHelper.parseInputDateTime(scanner.nextLine().trim());
 
            // Validate and calculate price
            double hours = calculateHours(start, end);
            if (hours <= 0) {
                System.out.println("End time must be after start time.");
                return;
            }
            if (hours > MAX_HOURS) {
                System.out.println("Maximum booking is " + MAX_HOURS + " hours. You entered " + hours + " hours.");
                return;
            }
 
            // Check if kitchen is available for that time slot
            if (!DatabaseHelper.isKitchenAvailable(kitchenId, start, end)) {
                System.out.println("Kitchen " + kitchenId + " is already booked or occupied for that time slot. Please choose a different time or kitchen.");
                return;
            }
 
            double price = hours * RATE_PER_HOUR;
 
            // Show summary before confirming
            System.out.println("\n--- Booking Summary ---");
            System.out.println("  Kitchen   : " + kitchenId);
            System.out.println("  Section   : " + section);
            System.out.println("  Start     : " + start);
            System.out.println("  End       : " + end);
            System.out.printf("  Duration  : %.1f hour(s)%n", hours);
            System.out.printf("  Price     : %s%,.2f (%.1f hrs x %s%,.2f/hr)%n",
                    PESO, price, hours, PESO, RATE_PER_HOUR);
            System.out.print("\nConfirm booking? (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (!confirm.equals("yes")) {
                System.out.println("Booking cancelled.");
                return;
            }
 
            Kitchen kitchen = new Kitchen.Builder()
                    .kitchenId(kitchenId)
                    .section(section)
                    .scheduleStart(start)
                    .scheduleEnd(end)
                    .price(price)
                    .username(loggedInUsername)
                    .build();
 
            if (DatabaseHelper.insertKitchen(kitchen)) {
                System.out.println("Kitchen Booking submitted! Waiting for admin approval.");
            } else {
                System.out.println("Failed to add booking.");
            }
 
        } catch (FinanceException | IllegalStateException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Kitchen ID and Section must be numbers.");
        }
    }
 
    // ---------------------------------------------------------------
    // KITCHEN — ADMIN: Edit
    // ---------------------------------------------------------------
    private static void editKitchenEntry() {
        System.out.println("\n--- Edit Kitchen Booking ---");
        viewKitchenSchedule();
        System.out.println("Schedule format: M/D/YYYY H:MMAM or H:MMPM  (e.g. 5/8/2026 1:00PM)");
        try {
            System.out.print("Enter the ID to edit: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("New Kitchen ID (1-4): ");
            int kitchenId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("New Section (1-8): ");
            if (kitchenId < 1 || kitchenId > 4) { System.out.println("Invalid Kitchen ID. Must be between 1 and 4."); return; }
            int section = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("New Availability (Occupied/Approved/Rejected): ");
            if (section < 1 || section > 8) { System.out.println("Invalid Section. Must be between 1 and 8."); return; }
            String availability = scanner.nextLine().trim();
            System.out.print("New Schedule Start: ");
            String start = DatabaseHelper.parseInputDateTime(scanner.nextLine().trim());
            System.out.print("New Schedule End:   ");
            String end = DatabaseHelper.parseInputDateTime(scanner.nextLine().trim());
 
            double hours = calculateHours(start, end);
            if (hours <= 0) { System.out.println("End time must be after start time."); return; }
            if (hours > MAX_HOURS) { System.out.println("Maximum booking is " + MAX_HOURS + " hours."); return; }
 
            double price = hours * RATE_PER_HOUR;
            System.out.printf("  Recalculated Price: %s%,.2f (%.1f hrs x %s%,.2f/hr)%n",
                    PESO, price, hours, PESO, RATE_PER_HOUR);
 
            Kitchen kitchen = new Kitchen.Builder()
                    .kitchenId(kitchenId)
                    .section(section)
                    .availability(availability)
                    .scheduleStart(start)
                    .scheduleEnd(end)
                    .price(price)
                    .username(loggedInUsername)
                    .build();
 
            if (DatabaseHelper.updateKitchen(id, kitchen))
                System.out.println("Kitchen Booking updated successfully.");
            else
                System.out.println("Failed to update entry.");
 
        } catch (FinanceException | IllegalStateException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. IDs must be numbers.");
        }
    }
 
    // ---------------------------------------------------------------
    // KITCHEN — ADMIN: Delete
    // ---------------------------------------------------------------
    private static void deleteKitchenEntry() {
        System.out.println("\n--- Delete Kitchen Booking ---");
        List<String> entries = DatabaseHelper.getAllKitchenEntries();
        if (entries.isEmpty()) {
            System.out.println("No kitchen bookings at the moment.");
            return;
        }
        for (String entry : entries) System.out.println(entry);
        try {
            System.out.print("Enter the ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            if (DatabaseHelper.deleteKitchen(id))
                System.out.println("Kitchen Booking deleted.");
            else
                System.out.println("Failed to delete entry.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
        }
    }
 
    // ---------------------------------------------------------------
    // KITCHEN — ADMIN: Approve (just approves, no payment)
    // ---------------------------------------------------------------
    private static void approveKitchenBooking() {
        System.out.println("\n--- Approve Kitchen Booking ---");
        viewKitchenSchedule();
        System.out.print("\nEnter Booking ID to approve: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Map<String, String> booking = DatabaseHelper.getKitchenById(id);
 
            if (booking == null) {
                System.out.println("Booking not found.");
                return;
            }
            if (!booking.get("availability").equals("Occupied")) {
                System.out.println("This booking is already " + booking.get("availability") + ".");
                return;
            }
 
            DatabaseHelper.updateKitchenAvailability(id, "Approved");
            System.out.println("Booking #" + id + " has been approved. Student can now proceed to payment.");
 
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID entered.");
        }
    }
 
    // ---------------------------------------------------------------
    // KITCHEN — STUDENT: Pay for Booking (uses Payment Framework)
    // ---------------------------------------------------------------
    private static void payForBooking() {
        System.out.println("\n--- Pay for Booking ---");
 
        List<Map<String, String>> approved = DatabaseHelper.getApprovedBookingsByUser(loggedInUsername);
 
        if (approved.isEmpty()) {
            System.out.println("No approved bookings to pay for.");
            return;
        }
 
        System.out.println("\nYour Approved Bookings:");
        System.out.println("================================================");
        System.out.printf("%-5s %-10s %-10s %-22s %-22s %-12s%n",
                "ID", "Kitchen", "Section", "Start", "End", "Price");
        System.out.println("------------------------------------------------");
        for (Map<String, String> b : approved) {
            double price = parseDoubleSafe(b.get("price"));
            System.out.printf("%-5s %-10s %-10s %-22s %-22s PHP %,.2f%n",
                    b.get("id"),
                    b.get("kitchen_id"),
                    b.get("section"),
                    b.get("scheduleStart"),
                    b.get("scheduleEnd"),
                    price);
        }
        System.out.println("================================================");
 
        System.out.print("\nEnter Booking ID to pay: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Map<String, String> booking = DatabaseHelper.getKitchenById(id);
 
            if (booking == null) {
                System.out.println("Booking not found.");
                return;
            }
            if (!booking.get("availability").equals("Approved")) {
                System.out.println("This booking is not approved for payment yet.");
                return;
            }
            if (!loggedInUsername.equals(booking.get("username"))) {
                System.out.println("This booking does not belong to your account.");
                return;
            }
 
            double price = parseDoubleSafe(booking.get("price"));
            String desc  = "Kitchen " + booking.get("kitchen_id") + " | Section " + booking.get("section");
 
            // --- Payment Method ---
            System.out.println("\nSelect Payment Method:");
            System.out.println("1. Cash");
            System.out.println("2. Credit Card");
            System.out.println("3. Debit Card");
            System.out.println("4. E-Wallet");
            System.out.print("Choose: ");
            PaymentFramework.PaymentMethod method;
            switch (scanner.nextLine().trim()) {
                case "1": method = PaymentFramework.PaymentMethod.CASH;        break;
                case "2": method = PaymentFramework.PaymentMethod.CREDIT_CARD; break;
                case "3": method = PaymentFramework.PaymentMethod.DEBIT_CARD;  break;
                case "4": method = PaymentFramework.PaymentMethod.E_WALLET;    break;
                default:
                    System.out.println("Invalid choice. Defaulting to Cash.");
                    method = PaymentFramework.PaymentMethod.CASH;
            }
 
            // --- Discounts ---
            System.out.print("PWD/Senior discount? (yes/no): ");
            boolean isPWD = scanner.nextLine().trim().equalsIgnoreCase("yes");
 
            boolean applyPromo = false;
            if (!isPWD) {
                System.out.print("Apply promo discount (10% off)? (yes/no): ");
                applyPromo = scanner.nextLine().trim().equalsIgnoreCase("yes");
            }
 
            // --- Process via Framework ---
            KitchenPayment payment = new KitchenPayment(id, loggedInUsername, desc);
            payment.processInvoice(price, false, isPWD, applyPromo, method);
            System.out.println("Payment successful! Your booking is now marked as Paid.");
 
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID entered.");
        } catch (IllegalArgumentException | IllegalStateException | FinanceException e) {
            System.out.println("Payment error: " + e.getMessage());
        }
    }
 
    // ---------------------------------------------------------------
    // KITCHEN — ADMIN: Reject
    // ---------------------------------------------------------------
    private static void rejectKitchenBooking() {
        System.out.println("\n--- Reject Kitchen Booking ---");
        viewKitchenSchedule();
        System.out.print("\nEnter Booking ID to reject: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Map<String, String> booking = DatabaseHelper.getKitchenById(id);
 
            if (booking == null) {
                System.out.println("Booking not found.");
                return;
            }
            if (!booking.get("availability").equals("Occupied")) {
                System.out.println("This booking is already " + booking.get("availability") + ".");
                return;
            }
 
            DatabaseHelper.updateKitchenAvailability(id, "Rejected");
            System.out.println("Booking #" + id + " has been rejected.");
 
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID entered.");
        }
    }
 
    // ---------------------------------------------------------------
    // ADMIN — Generate Student Report
    // ---------------------------------------------------------------
    private static void generateStudentReport() {
        System.out.println("\n=============================");
        System.out.println("      STUDENT REPORT         ");
        System.out.println("=============================");
 
        Map<String, List<String>> grouped = DatabaseHelper.getAllStudentsGroupedBySection();
 
        if (grouped.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
 
        for (Map.Entry<String, List<String>> entry : grouped.entrySet()) {
            System.out.println("\n--- Section " + entry.getKey() + " ---");
            for (String line : entry.getValue()) System.out.println(line);
        }
 
        System.out.println("\n=============================");
        System.out.println("Total sections with students: " + grouped.size());
    }
 
    // ---------------------------------------------------------------
    // ADMIN — Update Student Profile
    // ---------------------------------------------------------------
    private static void updateStudentProfile() {
        System.out.println("\n--- Update Student Profile ---");
        System.out.print("Enter the username of the student to update: ");
        String searchUsername = scanner.nextLine().trim();
 
        String[] current = DatabaseHelper.getStudentByUsername(searchUsername);
        if (current == null) {
            System.out.println("No student found with username: " + searchUsername);
            return;
        }
 
        System.out.println("\nCurrent Profile:");
        System.out.println("  Username   : " + current[0]);
        System.out.println("  First Name : " + current[1] + "  (not editable)");
        System.out.println("  Last Name  : " + current[2] + "  (not editable)");
        System.out.println("  Number     : " + current[3]);
        System.out.println("  Email      : " + current[4]);
        System.out.println("  Section    : " + current[5]);
        System.out.println("  Grade Lvl  : " + current[6]);
 
        System.out.println("\nEnter new values (press Enter to keep current value):");
 
        try {
            System.out.print("New Username [" + current[0] + "]: ");
            String newUsername = scanner.nextLine().trim();
            if (newUsername.isEmpty()) newUsername = current[0];
 
            System.out.print("New Password (leave blank to keep current): ");
            String newPassword = scanner.nextLine().trim();
            if (newPassword.isEmpty()) newPassword = DatabaseHelper.getPasswordByUsername(current[0]);
 
            System.out.print("New Number [" + current[3] + "]: ");
            String newNumber = scanner.nextLine().trim();
            if (newNumber.isEmpty()) newNumber = current[3];
 
            System.out.print("New Email [" + current[4] + "]: ");
            String newEmail = scanner.nextLine().trim();
            if (newEmail.isEmpty()) newEmail = current[4];
 
            System.out.print("New Section (1-8) [" + current[5] + "]: ");
            String newSection = scanner.nextLine().trim();
            if (newSection.isEmpty()) newSection = current[5];
 
            System.out.print("New Grade Level [" + current[6] + "]: ");
            String newGradeLvl = scanner.nextLine().trim();
            if (newGradeLvl.isEmpty()) newGradeLvl = current[6];
 
            try {
                int sectionInt = Integer.parseInt(newSection);
                if (sectionInt < 1 || sectionInt > 8)
                    throw new IllegalStateException("Section must be between 1 and 8.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid section. Must be a number between 1 and 8.");
                return;
            }
 
            if (DatabaseHelper.updateStudent(current[0], newUsername, newPassword,
                    newNumber, newEmail, newSection, newGradeLvl))
                System.out.println("Student profile updated successfully.");
            else
                System.out.println("Failed to update student profile.");
 
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
 
    // ---------------------------------------------------------------
    // HELPERS
    // ---------------------------------------------------------------
 
    // Calculates hours between two "YYYY-MM-DD HH:MM:00" strings
    private static double calculateHours(String start, String end) {
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startDt = LocalDateTime.parse(start, fmt);
            LocalDateTime endDt   = LocalDateTime.parse(end, fmt);
            long minutes = Duration.between(startDt, endDt).toMinutes();
            return minutes / 60.0;
        } catch (Exception e) {
            return 0;
        }
    }
 
    private static double parseDoubleSafe(String value) {
        try {
            return value != null ? Double.parseDouble(value) : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
 