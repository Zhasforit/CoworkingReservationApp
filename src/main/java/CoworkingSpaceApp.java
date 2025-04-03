import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CoworkingSpaceApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static String currentUser = null;
    private static boolean isAdmin = false;

    public static void main(String[] args) {
        initializeSampleData();
        runApplication();
    }

    private static void initializeSampleData() {
        Workspace.addWorkspace(1, "Private Office", 25.0);
        Workspace.addWorkspace(2, "Open Space", 15.0);
        Workspace.addWorkspace(3, "Meeting Room", 40.0);
    }

    private static void runApplication() {
        while (true) {
            showMainMenu();
        }
    }

    private static void showMainMenu() {
        System.out.println("\n=== Coworking Space Manager ===");
        System.out.println("1. Admin Login");
        System.out.println("2. User Login");
        System.out.println("3. Exit");
        System.out.print("Select an option: ");

        int choice = getIntInput("", 1, 3);

        switch (choice) {
            case 1 -> adminLogin();
            case 2 -> userLogin();
            case 3 -> {
                System.out.println("Thank you for using the system. Goodbye!");
                System.exit(0);
            }
        }
    }

    private static void adminLogin() {
        System.out.print("\nEnter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();

        if ("admin".equals(username) && "admin123".equals(password)) {
            currentUser = username;
            isAdmin = true;
            showAdminMenu();
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    private static void userLogin() {
        System.out.print("\nEnter your username: ");
        String username = scanner.nextLine().trim();

        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }

        currentUser = username;
        isAdmin = false;
        showUserMenu();
    }

    private static void showAdminMenu() {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Add workspace");
            System.out.println("2. Remove workspace");
            System.out.println("3. View all workspaces");
            System.out.println("4. View all reservations");
            System.out.println("5. Logout");
            System.out.print("Select an option: ");

            int choice = getIntInput("", 1, 5);

            switch (choice) {
                case 1 -> addWorkspace();
                case 2 -> removeWorkspace();
                case 3 -> browseWorkspaces();
                case 4 -> viewAllReservations();
                case 5 -> {
                    currentUser = null;
                    isAdmin = false;
                    return;
                }
            }
        }
    }

    private static void showUserMenu() {
        while (true) {
            System.out.println("\n=== User Menu ===");
            System.out.println("1. Browse workspaces");
            System.out.println("2. Make reservation");
            System.out.println("3. View my reservations");
            System.out.println("4. Cancel reservation");
            System.out.println("5. Logout");
            System.out.print("Select an option: ");

            int choice = getIntInput("", 1, 5);

            switch (choice) {
                case 1 -> browseWorkspaces();
                case 2 -> makeReservation();
                case 3 -> viewMyReservations();
                case 4 -> cancelReservation();
                case 5 -> {
                    currentUser = null;
                    return;
                }
            }
        }
    }

    private static void addWorkspace() {
        System.out.println("\n=== Add New Workspace ===");
        int id = getIntInput("Enter workspace ID: ", 1, Integer.MAX_VALUE);

        if (Workspace.findById(id) != null) {
            System.out.println("Workspace ID already exists.");
            return;
        }

        System.out.print("Enter workspace type: ");
        String type = scanner.nextLine().trim();
        double price = getDoubleInput("Enter hourly price: ", 0, Double.MAX_VALUE);

        try {
            Workspace.addWorkspace(id, type, price);
            System.out.println("Workspace added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void removeWorkspace() {
        System.out.println("\n=== Remove Workspace ===");
        browseWorkspaces();

        if (Workspace.getAllWorkspaces().isEmpty()) return;

        int id = getIntInput("Enter workspace ID to remove: ", 1, Integer.MAX_VALUE);
        Workspace.removeWorkspace(id);
        System.out.println("Workspace removed successfully!");
    }

    private static void browseWorkspaces() {
        System.out.println("\n=== Available Workspaces ===");
        List<Workspace> workspaces = Workspace.getAllWorkspaces();

        if (workspaces.isEmpty()) {
            System.out.println("No workspaces available.");
            return;
        }

        workspaces.forEach(System.out::println);
    }

    private static void makeReservation() {
        System.out.println("\n=== Make Reservation ===");
        List<Workspace> availableWorkspaces = Workspace.getAllWorkspaces()
                .stream()
                .filter(Workspace::isAvailable)
                .toList();

        if (availableWorkspaces.isEmpty()) {
            System.out.println("No workspaces available for booking.");
            return;
        }

        availableWorkspaces.forEach(System.out::println);
        int workspaceId = getIntInput("Enter workspace ID: ", 1, Integer.MAX_VALUE);
        LocalDate date = getDateInput("Enter date (YYYY-MM-DD): ");

        try {
            Reservation reservation = Reservation.create(currentUser, workspaceId, date);
            System.out.println("Reservation created successfully!");
            System.out.println(reservation);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewMyReservations() {
        System.out.println("\n=== My Reservations ===");
        List<Reservation> myReservations = Reservation.getByUser(currentUser);

        if (myReservations.isEmpty()) {
            System.out.println("You have no reservations.");
            return;
        }

        myReservations.forEach(System.out::println);
    }

    private static void viewAllReservations() {
        System.out.println("\n=== All Reservations ===");
        Map<String, List<Reservation>> allReservations = Reservation.getAllReservationsByUser();

        if (allReservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }

        allReservations.forEach((user, reservations) -> {
            System.out.println("\nUser: " + user);
            reservations.forEach(System.out::println);
        });
    }

    private static void cancelReservation() {
        System.out.println("\n=== Cancel Reservation ===");
        List<Reservation> myReservations = Reservation.getByUser(currentUser);

        if (myReservations.isEmpty()) {
            System.out.println("You have no reservations to cancel.");
            return;
        }

        myReservations.forEach(System.out::println);
        int resId = getIntInput("Enter reservation ID to cancel: ", 1, Integer.MAX_VALUE);

        try {
            Reservation.cancel(currentUser, resId);
            System.out.println("Reservation cancelled successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static int getIntInput(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.printf("Please enter a number between %d and %d\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static double getDoubleInput(String prompt, double min, double max) {
        while (true) {
            try {
                System.out.print(prompt);
                double input = Double.parseDouble(scanner.nextLine());
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.printf("Please enter a number between %.2f and %.2f\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return LocalDate.parse(scanner.nextLine());
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }
}