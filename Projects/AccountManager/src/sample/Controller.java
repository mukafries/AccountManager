package sample;


import Backend.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


import java.io.IOException;
import java.sql.JDBCType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;


public class Controller {

    @FXML
    private AnchorPane mainPane;

    @FXML
    private GridPane pieChartGridPane;

    @FXML
    private PieChart expensePieChart, incomePieChart;
    private ObservableList<PieChart.Data> expenseChartData = FXCollections.observableArrayList();
    private ObservableList<PieChart.Data> incomeChartData = FXCollections.observableArrayList();

    @FXML
    private LineChart lineChart;

    private XYChart.Series expenseSeries = new XYChart.Series<>();
    private XYChart.Series incomeSeries = new XYChart.Series<>();

    @FXML
    private BarChart barChart;

    @FXML
    private TableView incomeTable, expenseTable;

    @FXML
    private TableColumn iDate, iDescription, iRecipient, iPrice;

    @FXML
    private TableColumn eDate, eDescription, eRecipient, ePrice;

    @FXML
    private VBox chartVbox, tsactVbox ;

    @FXML
    JFXButton addExpenseBtn, addIncomeBtn, removeExpenseBtn, removeIncomeBtn, chartBtn, homeBtn;

    @FXML
    private Label avgDailyIncome, avgDailyExpense, netWorth, monthBudget,
            totalIncomeLabel, totalExpenseLabel, nameLabel, closeBtn;
    @FXML
    private ToggleGroup chartRadioBtn;
    @FXML
    private JFXRadioButton barChartRadio, pieChartRadio, lineChartRadio;

    DateTimeFormatter format =DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public void initialize(){
        incomeTable.setEditable(true);
        expenseTable.setEditable(true);
        handleTransactionChange(incomeTable);
        handleTransactionChange(expenseTable);

        nameLabel.setText(Profile.getMyProfile().getMiddle_firstName()+", "+ Profile.getMyProfile().getLastName());

        //Binds the Income tableview columns to the transaction type class
        iDate.setCellValueFactory(new PropertyValueFactory<Transaction,String>("dateTime"));
        iDescription.setCellValueFactory(new PropertyValueFactory<Transaction,String>("description"));
        iRecipient.setCellValueFactory(new PropertyValueFactory<Transaction,String>("recipient"));
        iPrice.setCellValueFactory(new PropertyValueFactory<Transaction,Integer>("price"));
        incomeTable.setItems(Income.getInstance().getIncomeList());

        //Binds the Expense tableview columns to the transaction type class
        eDate.setCellValueFactory(new PropertyValueFactory<Transaction,String>("dateTime"));
        eDescription.setCellValueFactory(new PropertyValueFactory<Transaction,String>("description"));
        eRecipient.setCellValueFactory(new PropertyValueFactory<Transaction,String>("recipient"));
        ePrice.setCellValueFactory(new PropertyValueFactory<Transaction,Integer>("price"));
        expenseTable.setItems(Expenses.getInstance().getExpenseList());

        //Set accepting new value on edit
        setCellEdit(iDescription,true);
        setCellEdit(iRecipient,true);
        setCellEdit(eDescription,true);
        setCellEdit(eRecipient,true);

        avgDailyIncome.setText(Income.getInstance().averageDailyIncome()+"");
        avgDailyExpense.setText(Expenses.getInstance().averageDailyExpense()+"");
        netWorth();

        setTotalIncome();
        setTotalExpense();

        addLineChartData();
        addBarChart();

        setExpensePieChart();
        setIncomePieChart();

        expenseSeries.setName("Expense Portfolio");
        incomeSeries.setName("Income Portfolio");

        toggleSwitch();
        chartRadioBtn.selectToggle(pieChartRadio);
    }

    public void handleClose() throws IOException {
        Income.getInstance().storeIncomeList();
        Expenses.getInstance().storeExpenseList();
        System.exit(0);
    }

    public void toggleSwitch(){
        chartRadioBtn.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (chartRadioBtn.getSelectedToggle() != null ){
                    if (newValue.equals(barChartRadio)){
                        barChart.setVisible(true);
                        lineChart.setVisible(false);
                        pieChartGridPane.setVisible(false);
                    }
                    if (newValue.equals(lineChart)){
                        lineChart.setVisible(true);
                        barChart.setVisible(false);
                        pieChartGridPane.setVisible(false);
                    }
                    if (newValue.equals(pieChartRadio)){
                        pieChartGridPane.setVisible(true);
                        barChart.setVisible(false);
                        lineChart.setVisible(false);
                    }
                }
            }
        });
    }

    @FXML
    public void handleStackPaneChange(ActionEvent event){
        if(event.getSource().equals(chartBtn)){
            tsactVbox.setVisible(false);
            chartVbox.setVisible(true);
        }
        else if(event.getSource().equals(homeBtn)){
            chartVbox.setVisible(false);
            tsactVbox.setVisible(true);
        }
    }

    public void setCellEdit(TableColumn tableColumn, boolean isYes){
        if(isYes) {
            tableColumn.setEditable(true);
            tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            tableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Transaction, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Transaction, String> cellEditEvent) {
                    Transaction tsact = ((Transaction) cellEditEvent.getTableView().getItems().
                            get(cellEditEvent.getTablePosition().getRow()));
                    if (tableColumn.equals(iDescription)) {
                                tsact.setDescription(cellEditEvent.getNewValue());
                    }else if(tableColumn.equals(iRecipient)){
                        tsact.setRecipient(cellEditEvent.getNewValue());
                    }else if (tableColumn.equals(eDescription)){
                        tsact.setDescription(cellEditEvent.getNewValue());
                    }else if(tableColumn.equals(eRecipient)){
                        boolean isData = expensePieChart.getData().removeIf(data -> (tsact.getPrice() ==data.getPieValue()) &&
                                (tsact.getRecipient().equals(data.getName())));
                        tsact.setRecipient(cellEditEvent.getNewValue());
                        expensePieChart.getData().add(new PieChart.Data(tsact.getRecipient(),tsact.getPrice()));
                    }
                    else{
                        tableColumn.setEditable(false);
                    }
                }
            });
        }
    }

    public XYChart.Series setSeries(XYChart.Series series, ObservableList<Transaction> list){
        if(series != null && list.size()>0){
            SortedList<Transaction> sortedList = new SortedList(list, new Comparator() {
                @Override
                public int compare(Object t1, Object t2) {
                    LocalDate dt1 = LocalDateTime.parse(((Transaction) t1).getDateTime(), format).toLocalDate();
                    LocalDate dt2 = LocalDateTime.parse(((Transaction) t2).getDateTime(), format).toLocalDate();

                    return dt1.compareTo(dt2);
                }
            });
            for(Transaction tsact : sortedList){
                LocalDateTime dt = LocalDateTime.parse(tsact.getDateTime(), format);
                series.getData().add(new XYChart.Data(dt.toString(),tsact.getPrice()));
            }
            return series;
        }
        return null;
    }

    public void  addBarChart(){
        barChart.getData().add(setSeries(expenseSeries,Expenses.getInstance().getExpenseList()));
        barChart.getData().add(setSeries(incomeSeries,Income.getInstance().getIncomeList()));
    }

    public void addLineChartData(){
        lineChart.getData().add(setSeries(expenseSeries,Expenses.getInstance().getExpenseList()));
        lineChart.getData().add(setSeries(incomeSeries,Income.getInstance().getIncomeList()));
    }


    public void setExpensePieChart(){
        for(Transaction tsact : Expenses.getInstance().getExpenseList()){
            expenseChartData.add(new PieChart.Data(tsact.getRecipient(),tsact.getPrice()));
        }
        expensePieChart.setLegendSide(Side.BOTTOM);
        expensePieChart.getData().setAll(expenseChartData);
        expensePieChart.setAnimated(true);
        expensePieChart.setTitle("EXPENSES");
        expensePieChart.setTitleSide(Side.TOP);
        expensePieChart.setVisible(true);
    }

    public void setIncomePieChart(){
        for(Transaction tsact : Income.getInstance().getIncomeList()){
            incomeChartData.add(new PieChart.Data(tsact.getRecipient(),tsact.getPrice()));
        }
        incomePieChart.getData().setAll(incomeChartData);
        incomePieChart.setLegendSide(Side.BOTTOM);
        incomePieChart.setAnimated(true);
        incomePieChart.setTitle("INCOME");
        incomePieChart.setTitleSide(Side.TOP);
        incomePieChart.setVisible(true);

    }

    public void handleTransactionChange(TableView tableView){
        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if(tableView.equals(incomeTable)) {
                    netWorth();
                }else if (tableView.equals(expenseTable)){
                    netWorth();
                }
            }
        });
    }

    @FXML
    public void handleSave() throws IOException {
        Income.getInstance().storeIncomeList();
        Expenses.getInstance().storeExpenseList();
    }


    public void setTotalIncome(){
        totalIncomeLabel.setText(Income.getInstance().totalIncome()+"");
    }


    public void setTotalExpense(){
        totalExpenseLabel.setText(Expenses.getInstance().totalExpense()+"");
    }

    @FXML
    public void netWorth(){
        netWorth.setText(Profile.getMyProfile().netWorth()+"");
    }

    private int findData(PieChart pieChart, Transaction tsact){
        for(PieChart.Data data: pieChart.getData()){
            if((tsact.getRecipient().equals(data.getName())) && (tsact.getPrice() == data.getPieValue() ) ){
                return pieChart.getData().indexOf(data);
            }
        }
        return -1;
    }

    @FXML
    public void handleRemove(ActionEvent event){
        Transaction tsact;
        if(event.getSource().equals(removeIncomeBtn)){
             tsact = (Transaction) incomeTable.getSelectionModel().getSelectedItem();
            Income.getInstance().getIncomeList().remove(tsact);
            setTotalIncome();
            netWorth();
            int find = findData(incomePieChart,tsact);
            System.out.println(incomePieChart.getData().get(find).getName()+" : "+ incomePieChart.getData().get(find).getPieValue());
            incomePieChart.getData().remove(find);
            System.out.println(tsact.getRecipient()+" : "+tsact.getPrice());
        }
        else if (event.getSource().equals(removeExpenseBtn)){
            tsact = (Transaction) expenseTable.getSelectionModel().getSelectedItem();
            Expenses.getInstance().deleteTransaction(tsact);
            setTotalExpense();
            netWorth();
            int find = findData(expensePieChart,tsact);
            System.out.println(expensePieChart.getData().get(find).getName()+" : "+expensePieChart.getData().get(find).getPieValue());
            expensePieChart.getData().remove(find);
            System.out.println(tsact.getRecipient()+" : "+tsact.getPrice());
        }
    }

    @FXML
    public void handleAddTransaction(ActionEvent e){

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        dialog.setTitle("Add Transaction");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newTransaction.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){
            NewTransactionController controller = fxmlLoader.getController();
            Transaction transaction = controller.processResult();
            if(e.getSource().equals(addIncomeBtn)){
                Income.getInstance().addTransaction(transaction);
                System.out.println("Added to Income list");
                avgDailyIncome.setText(Income.getInstance().averageDailyIncome()+"");
                netWorth();
                setTotalIncome();
            }
            if (e.getSource().equals(addExpenseBtn)){
                Expenses.getInstance().addTransaction(transaction);
                System.out.println("Added to expenses list");
                avgDailyExpense.setText(Expenses.getInstance().averageDailyExpense()+"");
                netWorth();
                setTotalExpense();
                expensePieChart.getData().add(new PieChart.Data(transaction.getRecipient(),transaction.getPrice()));
            }
        }else{
            System.out.println("You pressed cancel");
        }
    }

}
