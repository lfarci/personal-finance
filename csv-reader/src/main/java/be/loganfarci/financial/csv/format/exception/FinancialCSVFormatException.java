package be.loganfarci.financial.csv.format.exception;

public class FinancialCSVFormatException extends Exception {
    public FinancialCSVFormatException(String message) {
        super(message);
    }

    public FinancialCSVFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
