package be.loganfarci.financial.csv.format.exception;

public class RecordParserException extends FinancialCSVFormatException {
    public RecordParserException(String message) {
        super(message);
    }
    public RecordParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
