import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

// Class representing a user session; implements Serializable for object persistence
class UserSession implements Serializable {
    // serialVersionUID ensures version compatibility between serialization and deserialization
    private static final long serialVersionUID = 1L;

    private String userId;
    private String username;
    private String role;
    
    // transient keyword prevents sensitive session token from being saved to disk
    private transient String sessionToken;

    // Constructor to initialize user session details
    public UserSession(String userId, String username, String role, String sessionToken) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.sessionToken = sessionToken;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    @Override
    public String toString() {
        return String.format("UserSession[ID=%s, Name=%s, Role=%s, Token=%s]",
                userId, username, role, (sessionToken == null ? "null (not saved)" : sessionToken));
    }
}

public class SerializationDemo {
    public static void main(String[] args) {
        System.out.println("=== Object Serialization & Deserialization Demo ===");
        String filename = "user_sessions.ser";

        // Array of UserSession objects to serialize
        UserSession[] sessions = {
            new UserSession("U101", "niraj_patel", "Administrator", "TOK_SEC_9812A"),
            new UserSession("U102", "aarav_shah", "Manager", "TOK_SEC_5431B"),
            new UserSession("U103", "diya_joshi", "Auditor", "TOK_SEC_2219C")
        };

        // Display original session data before saving
        System.out.println("\n--- 1. Original User Sessions Before Serialization ---");
        for (UserSession s : sessions) {
            System.out.println("  " + s);
        }

        // Saving array of objects using ObjectOutputStream inside try-with-resources
        System.out.println("\n--- 2. Serializing Sessions Array to '" + filename + "' ---");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            // Writes the entire array object to the file
            oos.writeObject(sessions);
            System.out.println("  Successfully wrote " + sessions.length + " session objects to " + filename);
        } catch (IOException e) {
            System.err.println("  Serialization error: " + e.getMessage());
            return;
        }

        // Reading back objects from disk into a fresh array using ObjectInputStream
        System.out.println("\n--- 3. Deserializing Sessions from File into Fresh Objects ---");
        UserSession[] loadedSessions = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            // Reads and casts deserialized object back to UserSession[]
            loadedSessions = (UserSession[]) ois.readObject();
            System.out.println("  Successfully loaded sessions from " + filename);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("  Deserialization error: " + e.getMessage());
            return;
        }

        // Verifying deserialized values and transient field behavior
        System.out.println("\n--- 4. Verification of Loaded Objects ---");
        if (loadedSessions != null) {
            for (UserSession s : loadedSessions) {
                System.out.println("  " + s);
            }
        }

        System.out.println("\nResult Analysis:");
        System.out.println("  Persistent fields (userId, username, role) survived serialization.");
        System.out.println("  Transient field (sessionToken) was NOT saved and restored as null.");
    }
}
