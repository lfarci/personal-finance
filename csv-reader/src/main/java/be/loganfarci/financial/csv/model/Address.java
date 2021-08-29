package be.loganfarci.financial.csv.model;

public class Address {

    private final String streetAndNumber;
    private final Municipality municipality;

    public Address(String streetAndNumber, Municipality municipality) {
        this.streetAndNumber = streetAndNumber;
        this.municipality = municipality;
    }

    public String getStreetAndNumber() {
        return streetAndNumber;
    }

    public Municipality getMunicipality() {
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
