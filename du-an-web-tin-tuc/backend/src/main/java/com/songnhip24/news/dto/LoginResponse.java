public class LoginResponse {
    private final String message;

    public LoginResponse(String message) { this.message = message; }
    // Constructor — gán message khi tạo object

    public String getMessage() { return message; }
}