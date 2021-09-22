package be.loganfarci.financial.csv;

import be.loganfarci.financial.csv.format.FinancialCSVFormat;
import be.loganfarci.financial.csv.format.exception.FinancialCSVFormatException;
import be.loganfarci.financial.csv.model.Transactions;

import java.io.*;

public class FinancialCSVFileReader {

    private static byte[] getByteArrayInputStream(InputStream inputStream) throws FinancialCSVFormatException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            org.apache.commons.io.IOUtils.copy(inputStream, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new FinancialCSVFormatException("Could not get byte array input stream.", e);
        }

    }

    private static FinancialCSVFormat findFinancialCSVFormat(byte[] bytes) throws FinancialCSVFormatException {
        FinancialCSVFormat format;
        if (FinancialCSVFormat.BELFIUS.canRead(bytes)) {
            format = FinancialCSVFormat.BELFIUS;
        } else if (FinancialCSVFormat.SODEXO.canRead(bytes)) {
            format = FinancialCSVFormat.SODEXO;
        } else {
            format = null;
        }
        return format;
    }

    public static Transactions read(InputStream inputStream) throws FinancialCSVFormatException {
        byte[] bytes = getByteArrayInputStream(inputStream);
        FinancialCSVFormat format = findFinancialCSVFormat(bytes);
        if (format == null) {
            throw new FinancialCSVFormatException("Unknown file format.");
        } else {
            return format.parse(bytes);
        }
    }

}
