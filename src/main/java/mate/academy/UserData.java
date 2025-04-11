package mate.academy;

public record UserData(String userId, String details) {
    @Override
    public String toString() {
        return "UserData["
                + "userId="
                + userId
                + ", details="
                + details
                + ']';
    }
}
