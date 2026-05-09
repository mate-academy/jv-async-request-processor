package mate.academy;

public class UserData {
    private final String userId;
    private String details;

    public UserData(String userId, String details) {
        this.userId = userId;
        this.details = details;
    }

    public String userId() {
        return userId;
    }

    public String details() {
        return details;
    }

    @Override
    public String toString() {
        return "UserData{"
                + "userId=" + userId
                + ", details=" + details
                + '}';
    }
}
