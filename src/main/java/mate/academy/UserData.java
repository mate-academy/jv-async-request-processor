package mate.academy;

public class UserData {
    private final String userId;
    private final String details;

    public UserData(String userId, String details) {
        this.userId = userId;
        this.details = details;
    }

    public String getUserId() {
        return userId;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "UserData[userId=" + userId + ", details=" + details + "]";
    }
}
