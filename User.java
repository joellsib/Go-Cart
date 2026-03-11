// --- User.java ---
// A simple class (POJO) to hold user data
public class User {
    String username;
    String email;
    String phone;
    String password;

    public User(String username, String email, String phone, String password) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password; // In a real app, this MUST be hashed!
    }
}