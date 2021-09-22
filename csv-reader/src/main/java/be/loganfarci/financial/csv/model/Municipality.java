package be.loganfarci.financial.csv.model;

public class Municipality {

    private final String name;
    private final Integer zipCode;

    public Municipality(String name, Integer zipCode) {
        this.name = name;
        this.zipCode = zipCode;
    }

    public String getName() {
        return name;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    @Override
    public String toString() {
        return "Municipality{" +
                "name='" + name + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}
