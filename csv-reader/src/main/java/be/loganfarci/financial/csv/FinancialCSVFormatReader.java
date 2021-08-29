package be.loganfarci.financial.csv;

import be.loganfarci.financial.csv.format.BelfiusCSVFormat;
import be.loganfarci.financial.csv.format.FinancialCSVFormat;
import be.loganfarci.financial.csv.format.exception.FinancialCSVFormatException;
import be.loganfarci.financial.csv.model.Transaction;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class FinancialCSVFormatReader {

    private static final String CONFIGURATION_FILE_NAME = "configuration.properties";
    private static final String CONFIGURATION_PROPERTY_NAME = "csv.file.path";

    private final Iterable<CSVRecord> records;

    public FinancialCSVFormatReader(Reader reader) throws IOException {
        this.records = getFinancialCSVFormat().parse(reader);
    }

    private static CSVFormat getFinancialCSVFormat() {
        return CSVFormat.DEFAULT.builder()
                .setSkipHeaderRecord(true)
                .setDelimiter(';')
                .setTrim(true)
                .build();
    }

    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        ClassLoader loader = FinancialCSVFormat.class.getClassLoader();
        InputStream in = loader.getResourceAsStream(CONFIGURATION_FILE_NAME);
        properties.load(in);
        return properties;
    }

    public static void main(String[] args) throws FinancialCSVFormatException, IOException {
        Properties properties = loadProperties();
        String csvFilePath = properties.getProperty(CONFIGURATION_PROPERTY_NAME);

        Reader reader = new FileReader(csvFilePath, StandardCharsets.ISO_8859_1);

        BelfiusCSVFormat format = new BelfiusCSVFormat();
        Iterable<Transaction> transactions = format.parse(reader);

        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }

    }

}
