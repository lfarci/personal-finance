package be.loganfarci.financial.csv.model.owner;

public class Owner {

    private final String name;
    private final Address address;

    public Owner(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }
}
