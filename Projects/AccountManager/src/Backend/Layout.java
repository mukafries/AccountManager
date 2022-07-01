package Backend;

import javafx.collections.ObservableList;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class Layout {
    DateTimeFormatter formatter =DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    DecimalFormat df = new DecimalFormat("###.##");

    public boolean addTransaction(ObservableList<Transaction> transactions, Transaction t){
        return transactions.add(t);
    }
    public boolean removeTransaction(ObservableList<Transaction> transactions, Transaction t){
        return transactions.remove(t);
    }

    public double totalAmount(ObservableList<Transaction> tsact){
        double total =0;
        if(tsact.isEmpty()){
            return total;
        }
        for(Transaction t: tsact){
            total += t.getPrice();
        }
        return total;
    }

    public double averageDailyTransaction(ObservableList<Transaction> transactions){
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        int size = transactions.size();
        double sum = 0;
        LocalDateTime minDate, maxDate;
        minDate = LocalDateTime.now();
        maxDate = LocalDateTime.now();
        for (int i=0; i<size; i++){
            Transaction transaction = transactions.get(i);
            if(i==0){
                minDate = LocalDateTime.parse(transaction.getDateTime(), formatter1);
            }
            if(i==(size-1)){
                maxDate = LocalDateTime.parse(transaction.getDateTime(), formatter1);
            }
            sum += transaction.getPrice();
        }

        if(minDate.isEqual(maxDate)){
            return sum;
        }else{
            int dateDifference = maxDate.compareTo(minDate);
            if(dateDifference<=-1){
                dateDifference *= -1;
            }
            return ((sum)/dateDifference);
        }
    }
}
