package be.loganfarci.financial.csv;

import be.loganfarci.financial.csv.exception.ColumnParserException;

@FunctionalInterface
public interface ColumnParser<T> {
    T parse(String value) throws ColumnParserException;
}
