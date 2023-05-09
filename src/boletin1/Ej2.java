package boletin1;

import java.sql.*;

public class Ej2 {
    public static void main(String[] args) {
        String url="jdbc:mysql://localhost:3306/classicmodels?useSSL=false&serverTimezone=UTC";
        String usuario="root";
        String password="1234";
        try (Connection con = DriverManager.getConnection(url,usuario,password); Statement consulta = con.createStatement()) {

            ResultSet resultados = consulta.executeQuery("select productName as Nombre, MSRP as Precio from products where MSRP <400");

            while (resultados.next()){
                System.out.println(resultados.getString("Nombre") + " " + resultados.getDouble("Precio"));
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
