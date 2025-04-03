import java.time.LocalDate;
import java.util.*;

public class Reservation {
    private static final Map<String, List<Reservation>> reservationsByUser = new HashMap<>();

    private final int id;
    private final int workspaceId;
    private final String customerName;
    private final LocalDate date;

    private Reservation(int id, int workspaceId, String customerName, LocalDate date) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.customerName = customerName;
        this.date = date;
    }

    public int getId() { return id; }
    public int getWorkspaceId() { return workspaceId; }
    public String getCustomerName() { return customerName; }
    public LocalDate getDate() { return date; }

    public static Reservation create(String username, int workspaceId, LocalDate date) {
        Workspace ws = Workspace.findById(workspaceId);
        if (ws == null || !ws.isAvailable()) {
            throw new IllegalArgumentException("Invalid or unavailable workspace");
        }

        if (hasConflict(workspaceId, date)) {
            throw new IllegalArgumentException("Workspace already booked for this date");
        }

        List<Reservation> userReservations = reservationsByUser
                .computeIfAbsent(username, k -> new ArrayList<>());

        int newId = userReservations.size() + 1;
        Reservation reservation = new Reservation(newId, workspaceId, username, date);
        userReservations.add(reservation);
        ws.setAvailable(false);

        return reservation;
    }

    public static boolean hasConflict(int workspaceId, LocalDate date) {
        return reservationsByUser.values().stream()
                .flatMap(List::stream)
                .anyMatch(r -> r.workspaceId == workspaceId && r.date.equals(date));
    }

    public static List<Reservation> getByUser(String username) {
        return new ArrayList<>(reservationsByUser.getOrDefault(username, new ArrayList<>()));
    }

    public static Map<String, List<Reservation>> getAllReservationsByUser() {
        Map<String, List<Reservation>> copy = new HashMap<>();
        reservationsByUser.forEach((user, resList) ->
                copy.put(user, new ArrayList<>(resList)));
        return copy;
    }

    public static void cancel(String username, int reservationId) {
        List<Reservation> userReservations = reservationsByUser.get(username);
        if (userReservations == null) return;

        userReservations.removeIf(res -> {
            if (res.id == reservationId) {
                Workspace ws = Workspace.findById(res.workspaceId);
                if (ws != null) ws.setAvailable(true);
                return true;
            }
            return false;
        });
    }

    @Override
    public String toString() {
        return String.format("Reservation #%d: Workspace %d for %s on %s",
                id, workspaceId, customerName, date);
    }
}