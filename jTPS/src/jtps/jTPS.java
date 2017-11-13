package jtps;

import java.util.ArrayList;

/**
 * @author McKillaGorilla
 * @author Ritwik Banerjee
 */
public class jTPS {
    private ArrayList<jTPS_Transaction> transactions          = new ArrayList<>();
    private int                         mostRecentTransaction = -1;

    public jTPS() {}

    @SuppressWarnings("unused")
    public void clearAllTransactions() {
        transactions.clear();
    }

    public boolean hasTransactionToUndo() {
        return mostRecentTransaction >= 0;
    }

    public boolean hasTransactionToRedo() {
        return mostRecentTransaction < (transactions.size() - 1);
    }

    public void addTransaction(jTPS_Transaction transaction) {
        // IS THIS THE FIRST TRANSACTION?
        if (mostRecentTransaction < 0) {
            // DO WE HAVE TO CHOP THE LIST?
            if (transactions.size() > 0) {
                transactions = new ArrayList<>();
            }
            transactions.add(transaction);
        }
        // ARE WE ERASING ALL THE REDO TRANSACTIONS?
        else if (mostRecentTransaction < (transactions.size() - 1)) {
            transactions.set(mostRecentTransaction + 1, transaction);
            transactions = new ArrayList<>(transactions.subList(0, mostRecentTransaction + 2));
        }
        // IS IT JUST A TRANSACTION TO APPEND TO THE END?
        else {
            transactions.add(transaction);
        }
        doTransaction();
    }

    public void doTransaction() {
        if (hasTransactionToRedo()) {
            jTPS_Transaction transaction = transactions.get(mostRecentTransaction + 1);
            transaction.doTransaction();
            mostRecentTransaction++;
        }
    }

    public void undoTransaction() {
        if (hasTransactionToUndo()) {
            jTPS_Transaction transaction = transactions.get(mostRecentTransaction);
            transaction.undoTransaction();
            mostRecentTransaction--;
        }
    }

    public String toString() {
        StringBuilder text = new StringBuilder("--Number of Transactions: " + transactions.size() + "\n");
        text.append("--Current Index on Stack: ").append(mostRecentTransaction).append("\n");
        text.append("--Current Transaction Stack:\n");
        for (int i = 0; i <= mostRecentTransaction; i++) {
            jTPS_Transaction jT = transactions.get(i);
            text.append("----").append(jT.toString()).append("\n");
        }
        return text.toString();
    }
}