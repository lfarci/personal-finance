package be.loganfarci.financial.csv.format.parser.column;

import be.loganfarci.financial.csv.format.exception.ColumnParserException;

@FunctionalInterface
public interface ColumnParser<T> {
    T parse(String value) throws ColumnParserException;
}
