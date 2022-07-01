package sample;

import Backend.Expenses;
import Backend.Income;
import Backend.Profile;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    static Stage mainStage;
    static Stage appStage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Signup_in.fxml"));
        mainStage = primaryStage;
        mainStage.setTitle("Account Manager");
        mainStage.initStyle(StageStyle.UNDECORATED);
//        mainStage.setScene(new Scene(root, 1536, 864));
        mainStage.setScene(new Scene(root, 750, 580));
//        mainStage.setHeight(864);
//        mainStage.setWidth(1536);
//        mainStage.getScene().getStylesheets().add("CSS/allinone.css");
        mainStage.show();

        appStage = new Stage();
        Parent root2 =FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainWindow.fxml")));
        appStage.initStyle(StageStyle.UNDECORATED);
        appStage.setScene(new Scene(root2, 1536, 864));
        appStage.getScene().getStylesheets().add("CSS/allinone.css");
    }


    public static void main(String[] args) {
        Profile.getMyProfile().setMiddle_firstName("Blessing Iyanuoluwa");
        Profile.getMyProfile().setLastName("Adeyemi");
        launch(args);
    }

    @Override
    public void init() throws IOException {
        Income.getInstance().loadIncomeList();
        Expenses.getInstance().loadExpenseList();
    }

    @Override
    public void stop() throws Exception {
        Income.getInstance().storeIncomeList();
        Expenses.getInstance().storeExpenseList();
    }

    public static Stage getMainStage() {
        return mainStage;
    }
}
