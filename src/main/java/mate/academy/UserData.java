package mate.academy;

public record UserData(String userId, String details) {
    public UserData(String userId, String details) {
        this.userId = userId;
        this.details = details;
    }

    @Override
    public String toString() {
        return "UserData{"
                + "userId='"
                + userId + '\''
                + ", userName='"
                + details + '\''
                + '}';
    }
}
