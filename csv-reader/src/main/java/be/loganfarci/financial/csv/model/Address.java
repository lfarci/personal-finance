package be.loganfarci.financial.csv.model;

public class Address {

    private final String streetAndNumber;
    private final String municipality;

    public Address(String streetAndNumber, String municipality) {
        this.streetAndNumber = streetAndNumber;
        this.municipality = municipality;
    }

    public String getStreetAndNumber() {
        return streetAndNumber;
    }

    public String getMunicipality() {
        return municipality;
    }

    @Override
    public String toString() {
        return "Address{" +
                "streetAndNumber='" + streetAndNumber + '\'' +
                ", municipality='" + municipality + '\'' +
                '}';
    }
}
