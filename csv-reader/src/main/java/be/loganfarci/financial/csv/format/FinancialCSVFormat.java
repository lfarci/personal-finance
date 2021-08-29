package be.loganfarci.financial.csv.format;

import be.loganfarci.financial.csv.format.exception.FinancialCSVFormatException;
import be.loganfarci.financial.csv.model.Transactions;

import java.io.Reader;

public interface FinancialCSVFormat {

    void setDelimiter(char delimiter);
    void setTrim(boolean value);
    Transactions parse(Reader reader) throws FinancialCSVFormatException;

}
