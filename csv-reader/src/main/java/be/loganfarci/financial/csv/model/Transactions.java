package be.loganfarci.financial.csv.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Transactions implements Iterable<Transaction> {

    private final List<Transaction> transactions;

    public Transactions() {
        transactions = new ArrayList<>();
    }

    public void add(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public void forEach(Consumer<? super Transaction> action) {
        transactions.forEach(action);
    }

    @Override
    public Spliterator<Transaction> spliterator() {
        return transactions.spliterator();
    }

    @Override
    public Iterator<Transaction> iterator() {
        return transactions.iterator();
    }
}
