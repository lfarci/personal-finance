package be.loganfarci.financial.csv.model.owner;

public class Address {

    private final String streetAndNumber;
    private final String zipCode;

    public Address(String streetAndNumber, String zipCode) {
        this.streetAndNumber = streetAndNumber;
        this.zipCode = zipCode;
    }

    public String getStreetAndNumber() {
        return streetAndNumber;
    }

    public String getZipCode() {
        return zipCode;
    }
}
