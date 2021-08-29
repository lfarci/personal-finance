package be.loganfarci.financial.csv.model;

public class Owner {

    private final String name;
    private final Address address;

    public Owner(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public Owner(String name) {
        this(name, null);
    }

    public Owner() {
        this(null, null);
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "name='" + name + '\'' +
                ", address=" + address +
                '}';
    }
}
