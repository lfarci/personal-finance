package be.loganfarci.financial.csv;

import be.loganfarci.financial.csv.format.FinancialCSVFormat;
import be.loganfarci.financial.csv.format.exception.FinancialCSVFormatException;
import be.loganfarci.financial.csv.model.Transactions;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Objects;

public class FinancialCSVFileReader {

    private static InputStream getByteArrayInputStream(InputStream inputStream) throws FinancialCSVFormatException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            org.apache.commons.io.IOUtils.copy(inputStream, outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new FinancialCSVFormatException("Could not get byte array input stream.", e);
        }

    }

    private static FinancialCSVFormat findFinancialCSVFormat(InputStream inputStream) throws FinancialCSVFormatException {
        FinancialCSVFormat format = null;
        if (FinancialCSVFormat.BELFIUS.canRead(getByteArrayInputStream(inputStream))) {
            format = FinancialCSVFormat.BELFIUS;
        }
        if (FinancialCSVFormat.SODEXO.canRead(getByteArrayInputStream(inputStream))) {
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
