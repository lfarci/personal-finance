package be.loganfarci.financial.csv;

import be.loganfarci.financial.csv.exception.ColumnParserException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class FinancialCSV {

    private static final String CONFIGURATION_FILE_NAME = "configuration.properties";
    private static final String CONFIGURATION_PROPERTY_NAME = "csv.file.path";

    private static final String[] HEADERS = {
            "Compte", "Date de comptabilisation", "Numéro d'extrait", "Numéro de transaction", "Compte contrepartie", "Nom contrepartie contient", "Rue et numéro", "Code postal et localité", "Transaction", "Date valeur", "Montant", "Devise", "BIC", "Code pays", "Communications"
    };

    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        ClassLoader loader = FinancialCSV.class.getClassLoader();
        InputStream in = loader.getResourceAsStream(CONFIGURATION_FILE_NAME);
        properties.load(in);
        return properties;
    }

    private static CSVFormat getFinancialCSVFormat() {
        return CSVFormat.DEFAULT.builder()
                .setSkipHeaderRecord(true)
                .setDelimiter(';')
                .setTrim(true)
                .build();
    }

    public static void main(String[] args) throws IOException, ColumnParserException {
//        Properties properties = loadProperties();
//        String csvFilePath = properties.getProperty(CONFIGURATION_PROPERTY_NAME);
//
//        Reader reader = new FileReader(csvFilePath, StandardCharsets.ISO_8859_1);
//        Iterable<CSVRecord> records = getFinancialCSVFormat().parse(reader);
//
//        for (CSVRecord record : records) {
//
//        }

        String iban = ColumnParsers.IBAN.parse("BE04 0635 4162 8231");
        System.out.println(iban);

    }

}
