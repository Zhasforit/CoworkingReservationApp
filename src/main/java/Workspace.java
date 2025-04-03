import java.util.ArrayList;
import java.util.List;

public class Workspace {
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