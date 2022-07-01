package Backend;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction{
    private SimpleDoubleProperty price;
    private SimpleIntegerProperty quantity;
    private SimpleStringProperty description;
    private SimpleStringProperty recipient;
    private SimpleStringProperty dateTime;
    private DateTimeFormatter formatter;

    public Transaction(double price, int quantity, String description, String recipient) {
        formatter =DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.recipient = new SimpleStringProperty(recipient);
        this.description = new SimpleStringProperty(description);
        this.dateTime = new SimpleStringProperty(LocalDateTime.now().format(formatter));
    }

    public Transaction getTransaction(){
        return this;
    }
    public double getPrice() {
        return price.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public String getDescription() {
        return description.get();
    }

    public String getDateTime() {
        return dateTime.get();
    }


    public String getRecipient() {
        return recipient.get();
    }

    public void setPrice(int price) {
        this.price.set(price);
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setRecipient(String recipient) {
        this.recipient.set(recipient);
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime.set(dateTime.format(formatter));
    }



    @Override
    public String toString() {
        if(quantity.get() > 0) {
            return "Transaction{" +
                    "price=" + price +
                    ", quantity=" + quantity +
                    ", description='" + description + '\'' +
                    ", dateTime=" + getDateTime()+
                    '}';
        }else{
            return "Transaction{" +
                    "price=" + price +
                    ", description='" + description + '\'' +
                    ", dateTime=" + getDateTime() +
                    '}';
        }
    }

}
