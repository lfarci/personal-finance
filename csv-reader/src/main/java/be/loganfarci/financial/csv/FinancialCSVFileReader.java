package be.loganfarci.financial.csv;

import be.loganfarci.financial.csv.format.FinancialCSVFormat;
import be.loganfarci.financial.csv.format.exception.FinancialCSVFormatException;
import be.loganfarci.financial.csv.model.Transactions;

import java.io.*;
import java.util.Objects;

public class FinancialCSVFileReader {

    private static FinancialCSVFormat findFinancialCSVFormat(InputStream inputStream) throws FinancialCSVFormatException {
        FinancialCSVFormat format = null;
        if (FinancialCSVFormat.BELFIUS.canRead(inputStream)) {
            format = FinancialCSVFormat.BELFIUS;
        }
        if (FinancialCSVFormat.SODEXO.canRead(inputStream)) {
            format = FinancialCSVFormat.SODEXO;
        }
        return format;
    }

    public static Transactions read(InputStream inputStream) throws FinancialCSVFormatException {
        FinancialCSVFormat format = findFinancialCSVFormat(Objects.requireNonNull(inputStream));
        if (format == null) {
            throw new FinancialCSVFormatException("Unknown file format.");
        } else {
            return format.parse(inputStream);
        }
    }

}
