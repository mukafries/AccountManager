package Backend;

import java.awt.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        Profile profile = new
                Profile("Blessing","Iyanuoluwa Adeyemi", LocalDate.of(2002,05,9),
                "08035777561","blessing201880@outlook.com");
        profile.setLastMonthBalance();
        System.out.println(profile.getLastMonthBalance());

        System.out.println("Height: "+Toolkit.getDefaultToolkit().getScreenSize().height);
        System.out.println("Width: "+Toolkit.getDefaultToolkit().getScreenSize().width);
    }

    public Connection getConnection(){
        return null;
    }
}
