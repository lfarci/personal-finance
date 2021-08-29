package be.loganfarci.financial.csv;

import be.loganfarci.financial.csv.format.FinancialCSVFormat;
import be.loganfarci.financial.csv.format.exception.FinancialCSVFormatException;
import be.loganfarci.financial.csv.model.Transactions;

import java.io.*;

public class FinancialCSVFileReader {

    private static File requireValidExistingFile(File financialCSVFile) throws IOException {
        if (!financialCSVFile.exists()) {
            throw new IOException("File does not exist: " + financialCSVFile.getPath());
        }
        if (!financialCSVFile.isFile()) {
            throw new IOException("Not a regular file: " + financialCSVFile.getPath());
        }
        if (!financialCSVFile.canRead()) {
            throw new IOException("Cannot read from file: " + financialCSVFile.getPath());
        }
        return financialCSVFile;
    }

    private static FinancialCSVFormat findFinancialCSVFormat(File file) throws FinancialCSVFormatException {
        FinancialCSVFormat format = null;
        if (FinancialCSVFormat.BELFIUS.canRead(file)) {
            format = FinancialCSVFormat.BELFIUS;
        }
        if (FinancialCSVFormat.SODEXO.canRead(file)) {
            format = FinancialCSVFormat.SODEXO;
        }
        return format;
    }

    public static Transactions read(File financialCSVFile) throws IOException, FinancialCSVFormatException {
        File validExistingFile = requireValidExistingFile(financialCSVFile);
        FinancialCSVFormat format = findFinancialCSVFormat(validExistingFile);
        if (format == null) {
            throw new FinancialCSVFormatException("Unknown file format.");
        } else {
            return format.parse(validExistingFile);
        }
    }

}
