package at.htl;

public class Customer {
    public String customerId;
    public String salesOrg;
    public String name;
    public String city;
    public String street;
    public String country;

    public Customer() {

    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", salesOrg='" + salesOrg + '\'' +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
