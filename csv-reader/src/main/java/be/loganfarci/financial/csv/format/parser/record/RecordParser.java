package be.loganfarci.financial.csv.format.parser.record;

import be.loganfarci.financial.csv.format.exception.RecordParserException;
import org.apache.commons.csv.CSVRecord;

@FunctionalInterface
public interface RecordParser<T> {
        T parse(CSVRecord record) throws RecordParserException;
}
