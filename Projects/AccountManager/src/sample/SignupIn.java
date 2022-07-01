package sample;


import Backend.Profile;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;


public class SignupIn {

    Profile myProfile = Profile.getMyProfile();

    @FXML
    Label signUpLabel, signInLabel, pwordMsg, confirmPwordMsg, signInMsg, forgot, close;

    @FXML
    JFXTextField firstName, midName, lastName, telNumber, emailUp, username, signInUsername;

    @FXML
    JFXDatePicker birthDate;

    @FXML
    JFXPasswordField pwordSignUp, confirmPwordSignUp, pwordSignIn;

    @FXML
    AnchorPane  signUpPane, signUpPane2, signInPane;

    @FXML
    FontAwesomeIcon next, next2, previous, signInNext;

    public void initialize(){

        pwordSignIn.setPromptText("Enter password");
    }

    @FXML
    public void handleForgot(){
        System.out.println(myProfile.getUsername()+" , "+ myProfile.getPassword());
        forgot.setTextFill(Color.RED);
    }
    @FXML
    public void handleClose(){
        Main.mainStage.close();
        System.exit(0);
    }

    public boolean checkField(TextField textField){
        return textField.getText().isEmpty() && textField.getText().trim().isEmpty();
    }


    public boolean setUsername(){
        if(username.getText().trim().isEmpty()){
            username.setPromptText("field can not be empty");
            return false;
        }
        if(username.getText().trim().equals(firstName.getText().trim()) ||
            username.getText().trim().equals(lastName.getText().trim()) ||
            username.getText().trim().equals(midName.getText().trim())){
            System.out.println("Invalid!!!");
            System.out.println("Choose another username");
            username.clear();
            username.requestFocus();
            return false;
        }else{
            myProfile.setUsername(username.getText().trim());
            return true;
        }
    }

    public boolean pwordNameCheck() {
        if (pwordSignUp.getText().trim().equalsIgnoreCase(firstName.getText().trim())) {
            pwordMsg.setText("Password can't be same as first name");
            pwordMsg.setTextFill(Color.RED);
            pwordSignUp.clear();
            return false;
        } else if (pwordSignUp.getText().trim().equalsIgnoreCase(myProfile.getLastName())) {
            pwordMsg.setText("Password can't be same as last name");
            pwordMsg.setTextFill(Color.RED);
            pwordSignUp.clear();
            return false;
        } else if (pwordSignUp.getText().trim().equalsIgnoreCase(midName.getText().trim())) {
            pwordMsg.setText("Password can't be same as middle name");
            pwordMsg.setTextFill(Color.RED);
            pwordSignUp.clear();
            return false;
        }
        if (pwordSignUp.getText().trim().length() > 6) {
            myProfile.setPassword(pwordSignUp.getText().trim());
            return true;
        }else{
            pwordMsg.setText("Password should be more than 6 characters");
            return false;
        }
    }

    public boolean pwordConfirmation(){
        return pwordSignUp.getText().trim().equals(confirmPwordSignUp.getText().trim());
    }

    @FXML
    public void handlePwordCheck(){
        System.out.println(pwordSignUp.getText());
        if (pwordSignUp.getText().trim().length() < 6) {
            pwordSignUp.setFocusColor(Color.RED);
            pwordMsg.setTextFill(Color.RED);
            pwordMsg.setText("Password is too short");
        } else if (pwordSignUp.getText().trim().length() >= 6 && pwordSignUp.getText().trim().length() < 10) {
            pwordSignUp.setFocusColor(Color.ORANGE);
            pwordMsg.setText("Strength: WEAK");
            pwordMsg.setTextFill(Color.ORANGE);
        } else if (pwordSignUp.getText().trim().length() >= 10 && pwordSignUp.getText().trim().length() < 15) {
            pwordSignUp.setFocusColor(Color.YELLOW);
            pwordMsg.setText("Strength: MEDIUM");
            pwordMsg.setTextFill(Color.YELLOW);
        } else if (pwordSignUp.getText().trim().length() >= 15) {
            pwordSignUp.setFocusColor(Color.LIGHTGREEN);
            pwordMsg.setText("Strength: STRONG");
            pwordMsg.setTextFill(Color.LIGHTGREEN);
        }
    }

    public void nextClicked(Event event){
        if(event.getSource().equals(next)) {
            if ((checkField(firstName) || checkField(midName) ||
                    checkField(lastName) || checkField(telNumber) || checkField(emailUp))) {
                System.out.println("fill blank fields");
            } else {
                myProfile.setLastName(lastName.getText().trim());
                myProfile.setMiddle_firstName(midName.getText().trim()+" "+firstName.getText().trim());
                myProfile.setTelNumber(telNumber.getText().trim());
                myProfile.setEmailAddress(emailUp.getText());
                signUpPane.setVisible(false);
                signInPane.setVisible(false);
                signUpPane2.setVisible(true);
                System.out.println(myProfile.getLastName()+" "+ myProfile.getMiddle_firstName()+" "+
                        myProfile.getTelNumber());
            }
        }else if(event.getSource().equals(previous)){
            signUpPane2.setVisible(false);
            signUpPane.setVisible(true);
        }

        if (event.getSource().equals(next2)){
            if(pwordNameCheck() && setUsername()){
                if(pwordConfirmation()){
                    myProfile.setPassword(pwordSignUp.getText().trim());
                    System.out.println("Sign-Up Successful");
                    signUpPane2.setVisible(false);
                    signInPane.setVisible(true);
                }else {
                    confirmPwordSignUp.requestFocus();
                    confirmPwordMsg.setText("Confirm password incorrect");
                }
            }else{
                pwordSignUp.requestFocus();
            }
        }
    }

    @FXML
    public void handleSignInNext(){
        if (signInUsername.getText().equals(myProfile.getUsername()) &&
            pwordSignIn.getText().trim().equals(myProfile.getPassword())){
            signInMsg.setTextFill(Color.LIGHTGREEN);
            signInMsg.setText("Successfully logged in");
            Main.mainStage.close();
            Main.appStage.show();
        }else{
            signInUsername.requestFocus();
            signInMsg.setTextFill(Color.RED);
            signInMsg.setText("Incorrect Username and Password");
        }
    }

    @FXML
    public void signChoice(Event event){
        if (event.getSource().equals(signUpLabel))
        {
            signInPane.setVisible(false);
            signUpPane2.setVisible(false);
            signUpPane.setVisible(true);
        }
        else if(event.getSource().equals(signInLabel))
        {
            signUpPane2.setVisible(false);
            signUpPane.setVisible(false);
            signInPane.setVisible(true);
        }
    }
}