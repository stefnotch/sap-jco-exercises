package at.htl;

public class ServiceNotification {
    public String notification;
    public String description;

    public ServiceNotification() {

    }

    @Override
    public String toString() {
        return "ServiceNotification{" +
                "notification='" + notification + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
