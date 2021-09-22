# Financial CSV Reader

## Build
```bash
# Build and install the library to the local repository
> mvn clean install
```

## Usage
```java
public static void main(String[] args) {
    try {
        Transactions transactions=FinancialCSVFileReader.read(new File("data.csv"));
        for(Transaction transaction : transactions){
            System.out.println(transaction);
        }
    } catch (FinancialCSVFormatException e) {
        System.err.println(e.getMessage());
    }
}
```

## Supported file formats

### Belfius
- Compte
- Date de comptabilisation
- Numéro d'extrait
- Numéro de transaction
- Compte contrepartie
- Nom contrepartie contient
- Rue et numéro
- Code postal et localité
- Transaction
- Date valeur
- Montant
- Devise
- BIC
- Code pays
- Communications

### Sodexo

- Date
- Affilié
- Amount