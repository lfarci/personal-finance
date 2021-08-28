package be.loganfarci.financial.csv.model;

import be.loganfarci.financial.csv.model.owner.Owner;

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
}
