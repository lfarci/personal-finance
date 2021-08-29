package be.loganfarci.financial.csv.model;

public class BankAccount {

    private final Owner owner;
    private final String iban;
    private final String bic;

    public BankAccount(Owner owner, String iban, String bic) {
        this.owner = owner;
        this.iban = iban;
        this.bic = bic;
    }

    public Owner getOwner() {
        return owner;
    }

    public String getIban() {
        return iban;
    }

    public String getBic() {
        return bic;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "owner=" + owner +
                ", iban='" + iban + '\'' +
                ", bic='" + bic + '\'' +
                '}';
    }
}
