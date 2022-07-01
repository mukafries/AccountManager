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
import java.util.*;

public class Income extends Layout {

    private ObservableList<Transaction> Income;
    private static String filename = "IncomeData";
    private static Income instance =new Income();
    private double totalIncome;

    public static Income getInstance(){
        return instance;
    }

    private Income() {
        Income = FXCollections.observableArrayList();
    }

    public void addTransaction(Transaction t){
        addTransaction(Income,t);
    }

    public void deleteTransaction(Transaction t){
        removeTransaction(Income,t);
    }

    public ObservableList<Transaction> getIncomeList() {
        return Income;
    }

    public double averageDailyIncome(){
        return averageDailyTransaction(getIncomeList());
    }

    public double totalIncome(){
        this.totalIncome = Double.parseDouble(df.format(super.totalAmount(getIncomeList())));
        return this.totalIncome;
    }

    public double getTotalIncome(){
        return totalIncome;
    }


    public void storeIncomeList() throws IOException {
        Path path = Paths.get(filename);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try{
            Iterator<Transaction> iter = Income.iterator();
            while (iter.hasNext()){
                Transaction tsact = iter.next();
                bw.write(String.format("%s\t%s\t%s\t%s\t%s",
                        tsact.getPrice()+"",
                        tsact.getQuantity()+"",
                        tsact.getDescription(),
                        tsact.getRecipient(),
                        tsact.getDateTime()));
                bw.newLine();
            }
        }finally{
            if(bw!= null){
                bw.close();
            }
        }
    }

    public void loadIncomeList() throws IOException {
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);

        String input;
        try{
            while((input = br.readLine()) != null){
                String[] pieces = input.split("\t");
                double price = Double.parseDouble(pieces[0]);
                int quantity = Integer.parseInt(pieces[1]);
                String shortDesc = pieces[2];
                String recipient = pieces[3];
                LocalDateTime dateTime =LocalDateTime.parse(pieces[4], formatter);

                Transaction transaction = new Transaction(price,quantity,shortDesc,recipient);
                transaction.setDateTime(dateTime);

                Income.add(transaction);

            }
        }finally{
            if(br != null){
                br.close();
            }
        }
    }
}
