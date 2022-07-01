package Backend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Iterator;

public class Expenses extends Layout {
    private ObservableList<Transaction> Expense;
    private String filename ="Expenses";
    private static Expenses instance = new Expenses();

    public static Expenses getInstance() {
        return instance;
    }



    private Expenses() {
        Expense = FXCollections.observableArrayList();
    }

    public void addTransaction(Transaction t){
        addTransaction(Expense,t);
    }

    public void deleteTransaction(Transaction t) {
        removeTransaction(Expense, t);
    }

    public double averageDailyExpense(){
        return averageDailyTransaction(getExpenseList());
    }

    public ObservableList<Transaction> getExpenseList() {
        return Expense;
    }

    public double totalExpense() {
        return Double.parseDouble(df.format(super.totalAmount(getExpenseList())));
    }

    public void storeExpenseList() throws IOException {
        Path path = Paths.get(filename);
        BufferedWriter bw = Files.newBufferedWriter(path);

        try{
            Iterator<Transaction> iter = Expense.iterator();
            while(iter.hasNext()){
                Transaction tsact = iter.next();
                bw.write(String.format("%s\t%s\t%s\t%s\t%s",
                        tsact.getPrice()+"",
                        tsact.getQuantity()+"",
                        tsact.getDescription()+"",
                        tsact.getRecipient()+"",
                        tsact.getDateTime()));
                bw.newLine();
            }
        }finally {
            if(bw != null){
                bw.close();
            }
        }
    }

    public void loadExpenseList() throws IOException {
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);

        String input;
        try{
            while((input = br.readLine()) != null){
                String pieces[] = input.split("\t");

                double price = Double.parseDouble(pieces[0]);
                int quantity = Integer.parseInt((pieces[1]));
                String shortDesc = pieces[2];
                String recipient = pieces[3];
                LocalDateTime dateTime = LocalDateTime.parse(pieces[4], formatter);

                Transaction transaction = new Transaction(price, quantity, shortDesc, recipient);
                transaction.setDateTime(dateTime);

                Expense.add(transaction);
            }
        }finally {
            if(br != null){
                br.close();
            }
        }
    }
}
