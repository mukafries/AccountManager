package sample;

import Backend.Transaction;
//import com.sun.jdi.IntegerType;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;


//class IntegerFilter implements UnaryOperator<TextFormatter.Change>{
//    private final static Pattern DIGIT_PATTERN = Pattern.compile("\\d*");
//
//    @Override
//    public TextFormatter.Change apply(TextFormatter.Change aT){
//        return DIGIT_PATTERN.matcher(aT.getText()).matches() ? aT : null;
//    }
//}

public class NewTransactionController {

    @FXML
    private JFXTextField shortDesc, quantityField, priceField, recipientField;


    int count =0;
    public void initialize() {


        int defaultValue = 0;

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*") ) {
                return change;
            }
            if (text.matches(".*")){
                if(count<1){
                    count++;
                    return change;
                }
                System.out.println(count);
            }
            return null;
        };


        TextFormatter<String> formatter = new TextFormatter<>(filter);
        quantityField.setTextFormatter(formatter);
    }

    public Transaction processResult(){
        String description = shortDesc.getText().trim();
        int quantity;
        if( quantityField.getText().isEmpty()){
            quantity=0;
        }else{
            quantity = Integer.parseInt(quantityField.getText().trim());
        }
        double price = Double.parseDouble(priceField.getText().trim());
        String recipient = recipientField.getText().trim();
        return new Transaction(price,quantity,description,recipient);
    }

}
