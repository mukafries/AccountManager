package Backend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sun.security.util.Password;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

public class Profile {
    private String lastName;
    private String middle_firstName;
    private LocalDate DOB;
    private String telNumber;
    private String emailAddress;
    private double MonthBalance;
    private double lastMonthBalance;
    private static String username;
    private static String password;

    private Income income;
    private Expenses expenses;

    private static Profile myProfile = new Profile();

    DecimalFormat df = new DecimalFormat("###.##");

    private Profile() {
        this.income= Income.getInstance();
        this.expenses = Expenses.getInstance();
    }

    public static Profile getMyProfile() {
        return myProfile;
    }

    public Profile(String lastName, String middle_firstName, LocalDate DOB, String telNumber, String emailAddress) {
        this.lastName = lastName;
        this.middle_firstName = middle_firstName;
        this.DOB = DOB;
        this.telNumber = telNumber;
        this.emailAddress = emailAddress;
        this.income= Income.getInstance();
        this.expenses = Expenses.getInstance();
    }

    public double getTotalIncome(){
        return Income.getInstance().getTotalIncome();
    }

    public double netWorth(){
        double balance = income.totalIncome()-expenses.totalExpense();
        return balance;
    }

    public void setLastMonthBalance(){
        Month lastMonth = LocalDateTime.now().getMonth().minus(1);
        setMonthBalance(lastMonth);
    }

    public double setMonthBalance(Month month){
        ObservableList monthIncomeList = getMonthList(month,income.getIncomeList());
        ObservableList monthExpenseList = getMonthList(month,expenses.getExpenseList());
        double balance = income.totalAmount(monthIncomeList) - expenses.totalAmount(monthExpenseList);
        return balance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pWord) {
        this.password = pWord;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Profile.username = username;
    }

    public ObservableList<Transaction> getMonthList(Month month, ObservableList<Transaction> list){
        ObservableList<Transaction> monthList = FXCollections.observableArrayList();
        for(Transaction tsact: list){
            LocalDate date = LocalDate.parse(tsact.getDateTime());
            if(date.getMonth().equals(month)){
                monthList.add(tsact);
            }
        }
        return monthList;
    }


    public double getLastMonthBalance() {
        return lastMonthBalance;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddle_firstName() {
        return middle_firstName;
    }

    public void setMiddle_firstName(String middle_firstName) {
        this.middle_firstName = middle_firstName;
    }

    public LocalDate getDOB() {
        return DOB;
    }

    public void setDOB(LocalDate DOB) {
        this.DOB = DOB;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public double getMonthBalance() {
        return MonthBalance;
    }

    public void setMonthBalance(double monthBalance) {
        MonthBalance = monthBalance;
    }

//    public double getBudget() {
//        return budget;
//    }
//
//    public void setBudget(double budget) {
//        this.budget = budget;
//    }
}
