import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Workspace {
    private static final String FILE_PATH = "workspaces.txt";
    private static final List<Workspace> workspaces = new ArrayList<>();

    private final int id;
    private final String type;
    private final double price;
    private boolean available;

    public Workspace(int id, String type, double price) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.available = true;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return available; }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    // Static management methods
    public static void loadFromFile() {
        workspaces.clear();  // Очищаем текущие данные перед загрузкой
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            throw new FileNotFoundException("Workspaces file not found: " + FILE_PATH);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0]);
                    String type = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    boolean available = Boolean.parseBoolean(parts[3]);

                    Workspace ws = new Workspace(id, type, price);
                    ws.setAvailable(available);
                    workspaces.add(ws);
                }
            }
            System.out.println("Workspaces loaded successfully!");
        } catch (IOException e) {
            throw new FileNotFoundException("Error reading workspaces file", e);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing workspace data: " + e.getMessage());
        }
    }

    public static void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Workspace ws : workspaces) {
                writer.write(
                        ws.id + "," +
                                ws.type + "," +
                                ws.price + "," +
                                ws.available + "\n"
                );
            }
            System.out.println("Workspaces saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving workspaces: " + e.getMessage());
        }
    }

    public static void addWorkspace(int id, String type, double price) {
        if (findById(id) != null) {
            throw new IllegalArgumentException("Workspace ID already exists");
        }
        workspaces.add(new Workspace(id, type, price));
    }

    public static void removeWorkspace(int id) {
        workspaces.removeIf(ws -> ws.id == id);
    }

    public static Workspace findById(int id) {
        return workspaces.stream()
                .filter(ws -> ws.id == id)
                .findFirst()
                .orElse(null);
    }

    public static List<Workspace> getAllWorkspaces() {
        return new ArrayList<>(workspaces);
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Type: %-10s | Price: $%-6.2f | Status: %s",
                id, type, price, available ? "Available" : "Booked");
    }
}